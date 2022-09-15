package disinkt.postservice.mappers;

import disinkt.postservice.dtos.ImageDto;
import disinkt.postservice.entities.Image;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageDtoMapper {

    public ImageDto toDto(Image image) {
        return new ImageDto(image.getId(), image.getContent());
    }

    public List<ImageDto> toCollectionDto(List<Image> images) {
        return images.stream().map(this::toDto).collect(Collectors.toList());
    }

}
