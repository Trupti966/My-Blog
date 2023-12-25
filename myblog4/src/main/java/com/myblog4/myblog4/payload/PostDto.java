package com.myblog4.myblog4.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {


    private long id;

    @NotEmpty
    @Size(min = 10, message = "Post title should have atleast 5 characters")
    private String title;

    @NotEmpty
    @Size(min = 10, message = "Post description should have atleast 10 characters")
    private String description;

    @NotEmpty
    private String content;

}
