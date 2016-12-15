package nsoushi.json

import com.squareup.moshi.Moshi
import io.kotlintest.specs.ShouldSpec
import org.elasticsearch.common.xcontent.XContentFactory
import org.hamcrest.MatcherAssert.*
import org.hamcrest.core.Is.*
import org.junit.Test

/**
 * @author nsoushi
 */
class AggregationModelTest : ShouldSpec() {

    init {
        "getAggregationsQuery" {
            should("valid aggregation parameter") {

                val aggregations = AggregationModel.getAggregationsQuery()

                val builder = XContentFactory.jsonBuilder()
                builder.startObject()
                aggregations.toXContent(builder, org.elasticsearch.common.xcontent.ToXContent.EMPTY_PARAMS)
                builder.endObject()

                val json = builder.string()

                val actual = adapter.fromJson(json)

                actual.name shouldBe "aggs_post_id"
                actual.terms.field shouldBe "post_id"
                actual.terms.size shouldBe 0L

                actual.aggregations?.name shouldBe "aggs_category_id"
                actual.aggregations?.terms?.field shouldBe "category_id"
                actual.aggregations?.terms?.size shouldBe 0L

                actual.aggregations?.aggregations?.name shouldBe "aggs_user_id"
                actual.aggregations?.aggregations?.terms?.field shouldBe "user_id"
                actual.aggregations?.aggregations?.terms?.size shouldBe 0L
            }
        }
    }

    val adapter = Moshi.Builder().add(AggregationAdapter()).build().adapter(Aggregations::class.java)
}
