# AI Chatbot Backend (Long Polling) 안내

본 레포지토리는 Spring Boot 기반으로 식단/운동 관리 기능을 제공하며, 본 변경에서는 **Gemini 2.5-Flash**를 활용하는 AI 챗봇 백엔드를 추가했습니다. 모든 기능은 로그인한 사용자를 기준으로 동작하며, 기존 `CurrentUser.email()` 인증 흐름을 그대로 사용합니다.

## 1. 추가된 파일 및 주요 기능
- **도메인 로직**: `com.yumyumcoach.domain.ai.chatbot` 패키지 전반 (컨트롤러/서비스/이벤트/엔티티/DTO/매퍼)
- **비동기 처리**: 질문 생성 시 Job을 만들고 즉시 응답, 별도의 비동기 리스너가 Gemini 호출 후 결과를 저장
- **DB 스키마**: `ai_chat_conversations`, `ai_chat_messages`, `ai_chat_jobs` 테이블 추가 (`db/init.sql`)
- **문서화**: 본 README에 API 상세, Postman 테스트 가이드, 프론트 연동 지침, DB 설명을 모두 포함

## 2. 신규 API 목록 (요약)
| 기능 | Function | API Path | Header | HTTP Method |
| --- | --- | --- | --- | --- |
| 질문 생성 및 Job 발행 | `createChatJob` | `/api/ai/chatbot/questions` | `Bearer Token` | POST |
| Job 상태 폴링 | `getJobStatus` | `/api/ai/chatbot/jobs/{jobId}` | `Bearer Token`, `PathVariable` | GET |
| 대화 메시지 조회 | `getConversationMessages` | `/api/ai/chatbot/conversations/{conversationId}/messages` | `Bearer Token`, `PathVariable` | GET |

## 3. API 상세 (Notion 스타일)

### 1) 질문 생성 및 Job 발행
- **기능**: 사용자 질문을 저장하고, Gemini 호출을 위한 Job을 PENDING 상태로 생성합니다. (즉시 응답)

#### 요청 헤더
| 이름 | 값 | 비고 |
| --- | --- | --- |
| Authorization | `Bearer {accessToken}` | 필수 |

#### Request Body
```json
{
  "question": "아침에 먹을 단백질 위주의 간단한 메뉴 추천해줘",
  "conversationId": 15
}
```
- `question`: 필수, 공백 불가
- `conversationId`: 선택. 전달 시 본인 소유 대화인지 검증하며, 없으면 신규 대화를 생성합니다.

#### Response (201 Created)
```json
{
  "conversationId": 15,
  "jobId": 42,
  "assistantMessageId": 87,
  "status": "PENDING"
}
```
- 응답 직후 프론트에서 `jobId`를 사용해 폴링을 시작합니다.

#### 오류 케이스
- 🟥 400 Bad Request: `question`이 비어 있거나 공백인 경우 (`AI_CHAT_INVALID_QUESTION`)
- 🟥 404 Not Found: `conversationId`가 본인 소유가 아닌 경우 (`AI_CHAT_CONVERSATION_NOT_FOUND`)

---

### 2) Job 상태 폴링
- **기능**: Long polling용. Job 상태를 조회하며 완료/실패 시 답변 또는 오류를 함께 반환합니다.

#### 요청 헤더
| 이름 | 값 | 비고 |
| --- | --- | --- |
| Authorization | `Bearer {accessToken}` | 필수 |

#### Path Variable
- `jobId` (Long): 생성 응답에서 받은 Job 식별자

#### Response (200 OK)
```json
{
  "conversationId": 15,
  "jobId": 42,
  "assistantMessageId": 87,
  "status": "COMPLETED",
  "assistantStatus": "COMPLETE",
  "content": "아침 식사로는 ... (Gemini 답변)",
  "errorMessage": null
}
```
- `status`: `PENDING`, `COMPLETED`, `FAILED`
- `assistantStatus`: `PENDING`, `COMPLETE`, `ERROR`
- `content`: `COMPLETED`일 때 Gemini 응답 본문
- `errorMessage`: 실패 시 에러 사유 (Job 또는 메시지 에러 우선 반환)

#### 오류 케이스
- 🟥 404 Not Found: Job이 없거나 본인 소유가 아닌 경우 (`AI_CHAT_JOB_NOT_FOUND`)

---

### 3) 대화 메시지 조회 (페이지 이동 후 복원용)
- **기능**: 특정 `conversationId`에 속한 전체 메시지를 시간순으로 반환합니다.

#### 요청 헤더
| 이름 | 값 | 비고 |
| --- | --- | --- |
| Authorization | `Bearer {accessToken}` | 필수 |

#### Path Variable
- `conversationId` (Long): 클라이언트가 보관한 대화 식별자

#### Response (200 OK)
```json
{
  "conversationId": 15,
  "messages": [
    {
      "messageId": 86,
      "role": "USER",
      "status": "COMPLETE",
      "content": "아침 메뉴 추천해줘",
      "errorMessage": null,
      "createdAt": "2025-12-23T08:10:00"
    },
    {
      "messageId": 87,
      "role": "ASSISTANT",
      "status": "COMPLETE",
      "content": "아침 식사로는 ...",
      "errorMessage": null,
      "createdAt": "2025-12-23T08:10:02"
    }
  ]
}
```

#### 오류 케이스
- 🟥 404 Not Found: 대화가 없거나 본인 소유가 아닌 경우 (`AI_CHAT_CONVERSATION_NOT_FOUND`)

---

## 4. Postman 테스트 가이드 (상세)
1. **공통 준비**
   - `{{baseUrl}}` = 서버 주소 (예: `http://localhost:8080`)
   - `Authorization` 탭에 `Bearer {accessToken}` 입력 (로그인 후 획득한 토큰)
2. **질문 생성** (POST `/api/ai/chatbot/questions`)
   - Body → raw → JSON 선택
   - 예시 입력:
     ```json
     { "question": "오늘 저녁에 먹을 다이어트 식단 추천", "conversationId": null }
     ```
   - Send 후 `jobId`, `assistantMessageId`를 응답에서 기록
3. **폴링** (GET `/api/ai/chatbot/jobs/{{jobId}}`)
   - `jobId`를 Path Variable로 설정
   - `status`가 `COMPLETED`/`FAILED`가 될 때까지 1~2초 간격으로 재실행
4. **대화 복원** (GET `/api/ai/chatbot/conversations/{{conversationId}}/messages`)
   - `conversationId`는 질문 생성 응답의 값을 사용
   - 응답 메시지 배열을 확인하여 UI 복원

## 5. 프론트 연동 지침 (상세 플로우)
1. **질문 전송**
   - 사용자 입력 직후 UI에 사용자 메시지를 optimistic하게 추가
   - API 호출: POST `/api/ai/chatbot/questions` → `jobId`, `assistantMessageId`, `conversationId` 확보
2. **Long Polling**
   - `jobId`로 `/api/ai/chatbot/jobs/{jobId}`를 주기적으로 호출
   - `PENDING`이면 재호출, `COMPLETED`면 `assistantMessageId`에 해당하는 UI bubble을 답변으로 업데이트, `FAILED`면 실패 표시 후 재시도 버튼 제공
3. **페이지 이동/새로고침**
   - `conversationId`를 `sessionStorage/localStorage`에 보관
   - 진입 시 `/api/ai/chatbot/conversations/{conversationId}/messages` 호출 → 시간순 렌더링
4. **프롬프트 특징**
   - 과거 채팅 내용도 함께 전송하여 맥락을 유지하며, **사용자 질문 + 건강 정보 + 주간 통계 + 오늘 날짜/요일**과 결합
   - 답변은 항상 한국어 존댓말, 친절/정확/근거 제시, 불확실 시 단정 금지, 이전 말투/내용 일관성 유지
5. **모델 키**
   - `.env`(레포 루트)에 `gemini.api.key` 또는 `GMS_KEY`를 설정 (모델: `gemini-2.5-flash`)

## 6. 서버/로직 변경 사항
  - **비동기 이벤트**: `ChatJobRequestedEvent` + `ChatJobEventListener(@Async, AFTER_COMMIT)`로 Gemini 호출을 분리
  - **서비스**: `AiChatbotService`에서 질문 검증, 대화/메시지/Job 생성, Gemini 프롬프트 구성 및 응답 저장
  - **매퍼/SQL**: MyBatis 매퍼 3종(`AiChatConversationMapper`, `AiChatMessageMapper`, `AiChatJobMapper`) 및 XML 추가
  - **에러 코드**: `AI_CHAT_CONVERSATION_NOT_FOUND`, `AI_CHAT_JOB_NOT_FOUND`, `AI_CHAT_INVALID_QUESTION` 신설
  - **프롬프트 구성 요소**: 건강 정보(`UserService#getMyPage`), 주간 통계(`WeeklyStatsService#getWeeklyStats`), 오늘 날짜/요일, 전체 대화 맥락, 최신 사용자 질문을 함께 전달

## 7. DB 스키마 추가 설명
- **ai_chat_conversations**: 사용자별 대화 스레드 (FK: accounts.email)
- **ai_chat_messages**: 대화 메시지 저장 (role=`USER`/`ASSISTANT`, status=`PENDING`/`COMPLETE`/`ERROR`)
- **ai_chat_jobs**: Gemini 호출 작업 관리 (status=`PENDING`/`COMPLETED`/`FAILED`), 메시지 FK로 연결

이 문서만으로 AI 챗봇 백엔드 구조, API 사용법, 테스트 및 프론트 연동 방법을 모두 확인할 수 있습니다.
