package games.poker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileData {

    private String name;

    private String url;

    @Override
    public String toString() {
        return name;
    }
}
