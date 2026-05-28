package academy.devdojo.anime;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/animes")
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeMapper mapper;
    private final AnimeService service;


    @GetMapping()
    public ResponseEntity<List<AnimeGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all anime, param name '{}'", name);
        var animes = service.findAll(name);
        var animeGetResponses = mapper.toAnimeGetResponseList(animes);
        return ResponseEntity.ok(animeGetResponses);
    }


    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id :{}", id);
        var anime = service.findByIdOrThrowNotFound(id);
        var animeGetResponse = mapper.toAnimeGetResponse(anime);
        return ResponseEntity.ok(animeGetResponse);
    }


    //Idempotente
    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody @Valid AnimePostRequest request) {
        log.debug("{}", request);
        var anime = mapper.toAnime(request);
        var animeSaved = service.save(anime);
        var animeGetResponse = mapper.toAnimePostResponse(animeSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(animeGetResponse);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest request) {
        log.debug("Request to update anime : {}", request);
        var animeToUpdate = mapper.toAnime(request);
        service.update(animeToUpdate);
        return ResponseEntity.noContent().build();
    }


}
