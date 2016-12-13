package nsoushi.json

import com.squareup.moshi.Moshi
import org.elasticsearch.common.xcontent.XContentFactory
import org.hamcrest.MatcherAssert.*
import org.hamcrest.core.Is.*
import org.junit.Test

/**
 * @author nsoushi
 */
class AggregationModelTest {

    val adapter = Moshi.Builder().add(AggregationAdapter()).build().adapter(Aggregations::class.java)

    @Test fun test_getAggregationsQuery() {

        val aggregations = AggregationModel.getAggregationsQuery()

        val builder = XContentFactory.jsonBuilder()
        builder.startObject()
        aggregations.toXContent(builder, org.elasticsearch.common.xcontent.ToXContent.EMPTY_PARAMS)
        builder.endObject()

        val json = builder.string()

        val actual = adapter.fromJson(json)

        assertThat(actual.name, `is`("aggs_post_id"))
        assertThat(actual.terms.field, `is`("post_id"))
        assertThat(actual.terms.size, `is`(0L))

        assertThat(actual.aggregations?.name, `is`("aggs_category_id"))
        assertThat(actual.aggregations?.terms?.field, `is`("category_id"))
        assertThat(actual.aggregations?.terms?.size, `is`(0L))

        assertThat(actual.aggregations?.aggregations?.name, `is`("aggs_user_id"))
        assertThat(actual.aggregations?.aggregations?.terms?.field, `is`("user_id"))
        assertThat(actual.aggregations?.aggregations?.terms?.size, `is`(0L))
    }
}