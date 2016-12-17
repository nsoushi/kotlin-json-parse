package nsoushi.json

import org.elasticsearch.search.aggregations.AggregationBuilders

/**
 *
 * @author nsoushi
 */
object AggregationModel {

    fun getAggregationsQuery() = AggregationBuilders.terms("aggs_post_id").field("post_id").size(1000)
            .subAggregation(AggregationBuilders.terms("aggs_category_id").field("category_id").size(20)
                    .subAggregation(AggregationBuilders.terms("aggs_user_id").field("user_id").size(2500)))!!
}
