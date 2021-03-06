package nsoushi.json

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 *
 * @author nsoushi
 */
@RestController
@EnableAutoConfiguration
@ComponentScan
@RequestMapping("/test")
open class TestController {

    @RequestMapping(value = "/content_list", method = arrayOf(RequestMethod.GET))
    open fun getContentList(): ResponseEntity<List<Post>> {
        val list = listOf(Post(1324231431L, 11L, User(1413241L, "John", 20)),
                Post(1321231341L, 22L, User(1453124L, "Amy", 25)),
                Post(1329709858L, 33L, User(1409709L, "Jessica", 38)))
        return ResponseEntity(list, HttpStatus.OK)
    }

    @RequestMapping(value = "/member/{id}", method = arrayOf(RequestMethod.GET))
    open fun getUser(@PathVariable id: Long): ResponseEntity<Member> {
        return ResponseEntity(Member(id, "name", 20, true), HttpStatus.OK)
    }

}


data class Post(
        val postId: Long,
        val categoryId: Long,
        val user: User
)

data class User(
        val userId: Long,
        val name: String,
        val age: Int
)

data class Member(
        val userId: Long,
        val name: String,
        @get:JsonIgnore
        val age: Int,
        @JsonProperty("isGold")
        val gold: Boolean = false
)
