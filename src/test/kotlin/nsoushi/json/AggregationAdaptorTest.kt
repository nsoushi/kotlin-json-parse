package nsoushi.json

import com.squareup.moshi.Moshi
import org.hamcrest.MatcherAssert.*
import org.hamcrest.core.Is.*
import org.junit.Test

/**
 *
 * @author nsoushi
 */
class AggregationAdaptorTest {

    val adapter = Moshi.Builder().add(AggregationAdapter()).build().adapter(Aggregations::class.java)

    @Test fun testFromJson() {
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

        val actual = adapter.fromJson(json)

        assertThat(actual.name, `is`("aggs_post_id"))
        assertThat(actual.terms.field, `is`("post_id"))
        assertThat(actual.terms.size, `is`(100L))

        assertThat(actual.aggregations?.name, `is`("aggs_category_id"))
        assertThat(actual.aggregations?.terms?.field, `is`("category_id"))
        assertThat(actual.aggregations?.terms?.size, `is`(200L))

        assertThat(actual.aggregations?.aggregations?.name, `is`("aggs_user_id"))
        assertThat(actual.aggregations?.aggregations?.terms?.field, `is`("user_id"))
        assertThat(actual.aggregations?.aggregations?.terms?.size, `is`(300L))
    }

    @Test fun testToJson() {

        val subAggsUser = Aggregations("aggs_user_id", Terms("user_id", 300L), null)
        val subAggsCategory = Aggregations("aggs_category_id", Terms("category_id", 200L), subAggsUser)
        val aggs = Aggregations("aggs_post_id", Terms("post_id", 100L), subAggsCategory)

        val actual = adapter.toJson(aggs)
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
        assertThat(actual, `is`(json))
    }
}