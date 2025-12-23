package com.yumyumcoach.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMyBasicInfoRequest {

    private String username;         // accounts.username (optional)
    private String profileImageUrl;  // profiles.profile_image_url (optional)
    private String introduction;     // profiles.introduction (optional)

    public boolean hasAnyValue() {
        return username != null || profileImageUrl != null || introduction != null;
    }
}
