package nsoushi.json

import com.beust.klaxon.*
import io.kotlintest.specs.BehaviorSpec
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

/**
 *
 * @author nsoushi
 */
@WebAppConfiguration
open class TestControllerTest : BehaviorSpec() {

    lateinit var mvc: MockMvc
    lateinit var target: TestController

    init {
        given("GET: /test/content_list") {

            target = TestController()
            mvc = MockMvcBuilders.standaloneSetup(target).build()

            val response = mvc.perform(MockMvcRequestBuilders.get("/test/content_list"))
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().response.contentAsString

            `when`("response is ok") {

                val array = Parser().parse(response.byteInputStream()) as JsonArray<JsonObject>

                then("レスポンスに含まれるPostは `3つ`") {
                    val postIds = array.long("postId")
                    postIds.size shouldBe 3
                }

                then("categoryIdが30以上のPostは `1つ`でpostIdは `1329709858`") {
                    val post = array.filter {
                        it.long("categoryId")!! > 30L
                    }
                    post.size shouldBe 1
                    post.get(0).long("postId") shouldBe 1329709858L
                }

                then("categoryIdが30以下のPostでuserのageが20以上のうち最後のレスポンスのuserのnameは `Amy`") {
                    val post = array.filter {
                        it.long("categoryId")!! < 30
                    }.findLast {
                        it.obj("user")!!.int("age")!! > 20
                    }!!

                    post.obj("user")!!.string("name") shouldBe "Amy"
                }
            }
        }
    }

    init {
        val userId: Long = 1
        given("GET: /test/member/{$userId}") {

            target = TestController()
            mvc = MockMvcBuilders.standaloneSetup(target).build()

            val response = mvc.perform(MockMvcRequestBuilders.get("/test/member/${userId}"))
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().response.contentAsString

            `when`("response is ok") {

                val user = Parser().parse(response.byteInputStream()) as JsonObject

                then("@JsonPropertyと@JsonIgnoreが有効であること") {
                    val gold = user.boolean("isGold")
                    gold shouldBe true

                    val containsAge = user.containsKey("age")
                    containsAge shouldBe false
                }
            }
        }
    }
}
