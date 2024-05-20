package com.sid.gl.catalogcontext.mapper;

import com.sid.gl.catalogcontext.domain.SongContent;
import com.sid.gl.catalogcontext.dtos.SaveSongDTO;
import com.sid.gl.catalogcontext.dtos.SongContentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongContentMapper {
    @Mapping(source = "song.publicId", target = "publicId")
    SongContentDTO songContentToSongContentDTO(SongContent songContent);

    SongContent saveSongDTOToSong(SaveSongDTO songDTO);
}
