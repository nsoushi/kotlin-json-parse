package nsoushi.json

import org.elasticsearch.search.aggregations.AggregationBuilders

/**
 *
 * @author nozawa_soushi
 */
object AggregationModel {

    fun getAggregations() = AggregationBuilders.terms("aggs_post_id").field("post_id").size(0)
            .subAggregation(AggregationBuilders.terms("aggs_category_id").field("category_id").size(0)
                    .subAggregation(AggregationBuilders.terms("aggs_user_id").field("user_id").size(0)))!!
}
