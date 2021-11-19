package br.com.alura.bookstore.controller;

import br.com.alura.bookstore.dto.UserDetailsDto;
import br.com.alura.bookstore.dto.UserDto;
import br.com.alura.bookstore.dto.UserFormDto;
import br.com.alura.bookstore.dto.UserUpdateFormDto;
import br.com.alura.bookstore.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/users")
@Api(tags = "User")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ApiOperation("Register new user")
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserFormDto dto, UriComponentsBuilder uriBuilder) {

        UserDto userDto = userService.register(dto);

        URI uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();

        return ResponseEntity.created(uri).body(userDto);
    }

    @GetMapping
    @ApiOperation("List of users")
    public Page<UserDto> list(@PageableDefault(size = 10) Pageable paging) {
        return userService.list(paging);
    }

    @GetMapping("/{id}")
    @ApiOperation("Consult a specific user")
    public ResponseEntity<UserDetailsDto> detail(@PathVariable @NotNull Long id) {

        UserDetailsDto detailsDto = userService.detail(id);

        return ResponseEntity.ok(detailsDto);
    }

    @PutMapping
    @ApiOperation("Update info about a specific user")
    public ResponseEntity<UserDetailsDto> update(@RequestBody @Valid UserUpdateFormDto dto) {

        UserDetailsDto detailsDto = userService.update(dto);

        return ResponseEntity.ok(detailsDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete user")
    public ResponseEntity<?> delete(@PathVariable @NotNull Long id) {

        userService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
