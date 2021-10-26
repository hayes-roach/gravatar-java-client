package gravatar;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserImage {
    private int rating;
    private String url;
}
