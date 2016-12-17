package nsoushi.json

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.int
import com.beust.klaxon.string
import io.kotlintest.specs.ShouldSpec
import org.elasticsearch.common.xcontent.XContentFactory

/**
 * @author nsoushi
 */
class AggregationModelUsingKlaxonTest : ShouldSpec() {

    init {
        "getAggregationsQuery" {
            "valid aggregation parameter" {

                val aggregations = AggregationModel.getAggregationsQuery()

                val builder = XContentFactory.jsonBuilder()
                builder.startObject()
                aggregations.toXContent(builder, org.elasticsearch.common.xcontent.ToXContent.EMPTY_PARAMS)
                builder.endObject()

                val json = builder.string()

                val parser: Parser = Parser()
                val jsonObject = parser.parse(json.byteInputStream()) as JsonObject

                should("valid aggs_post_id.terms") {
                    val aggsPostIdTerms = (jsonObject.get("aggs_post_id") as JsonObject).get("terms") as JsonObject

                    aggsPostIdTerms.string("field") shouldBe "post_id"
                    aggsPostIdTerms.int("size") shouldBe 1000
                }

                should("valid aggs_category_id.terms") {
                    val aggsCategoryId = ((jsonObject.get("aggs_post_id") as JsonObject).get("aggregations") as JsonObject).get("aggs_category_id") as JsonObject
                    val aggCategoryIdTerms = aggsCategoryId.get("terms") as JsonObject

                    aggCategoryIdTerms.string("field") shouldBe "category_id"
                    aggCategoryIdTerms.int("size") shouldBe 20
                }

                should("valid aggs_user_id.terms") {
                    val aggsUserId = ((((jsonObject.get("aggs_post_id") as JsonObject).get("aggregations") as JsonObject).get("aggs_category_id") as JsonObject).get("aggregations") as JsonObject).get("aggs_user_id") as JsonObject
                    val aggUserIdTerms = aggsUserId.get("terms") as JsonObject

                    aggUserIdTerms.string("field") shouldBe "user_id"
                    aggUserIdTerms.int("size") shouldBe 2500
                }
            }
        }
    }
}
