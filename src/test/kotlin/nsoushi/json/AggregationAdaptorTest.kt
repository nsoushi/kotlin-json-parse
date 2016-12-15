package nsoushi.json

import com.squareup.moshi.Moshi
import io.kotlintest.specs.ShouldSpec
import org.hamcrest.MatcherAssert.*
import org.hamcrest.core.Is.*
import org.junit.Test

/**
 *
 * @author nsoushi
 */
class AggregationAdaptorTest : ShouldSpec() {

    init {
        "fromJson" {
            should("valid convert to object") {

                val actual = adapter.fromJson(json)

                actual.name shouldBe "aggs_post_id"
                actual.terms.field shouldBe "post_id"
                actual.terms.size shouldBe 100L

                actual.aggregations?.name shouldBe "aggs_category_id"
                actual.aggregations?.terms?.field shouldBe "category_id"
                actual.aggregations?.terms?.size shouldBe 200L

                actual.aggregations?.aggregations?.name shouldBe "aggs_user_id"
                actual.aggregations?.aggregations?.terms?.field shouldBe "user_id"
                actual.aggregations?.aggregations?.terms?.size shouldBe 300L
            }
        }

        "toJson" {
            should("valid convert to json") {

                val subAggsUser = Aggregations("aggs_user_id", Terms("user_id", 300L), null)
                val subAggsCategory = Aggregations("aggs_category_id", Terms("category_id", 200L), subAggsUser)
                val aggs = Aggregations("aggs_post_id", Terms("post_id", 100L), subAggsCategory)

                val actual = adapter.toJson(aggs)

                actual shouldBe json
            }
        }
    }

    val adapter = Moshi.Builder().add(AggregationAdapter()).build().adapter(Aggregations::class.java)
    val json = """{
    "aggs_post_id": {
        "terms": {
            "field": "post_id",
            "size": 100
        },
        "aggregations": {
            "aggs_category_id": {
                "terms": {
                    "field": "category_id",
                    "size": 200
                },
                "aggregations": {
                    "aggs_user_id": {
                        "terms": {
                            "field": "user_id",
                            "size": 300
                        }
                    }
                }
            }
        }
    }
}"""
}
