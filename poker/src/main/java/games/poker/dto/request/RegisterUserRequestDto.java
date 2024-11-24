package games.poker.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequestDto {

    private UserProfile profile;
    private UserCredentials credentials;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserProfile {
        private String firstName;
        private String lastName;
        private String email;
        private String login;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserCredentials {

        private UserPassword password;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        static class UserPassword {
            private String value;
        }
    }
}
