package org.exoplatform.addon.analytics.model;

import static org.exoplatform.addon.analytics.model.search.AnalyticsFieldFilterType.*;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.lang.StringUtils;

import org.exoplatform.addon.analytics.model.aggregation.AnalyticsAggregation;
import org.exoplatform.addon.analytics.model.search.AnalyticsFieldFilter;
import org.exoplatform.addon.analytics.model.search.Range;

import groovy.transform.ToString;
import lombok.Data;

@Data
@ToString
public class AnalyticsFilter implements Serializable {

  private static final long          serialVersionUID    = 5699550622069979910L;

  private List<AnalyticsFieldFilter> filters             = new ArrayList<>();

  private String                     multipleChartsField = null;

  private List<AnalyticsAggregation> xAxisAggregations   = new ArrayList<>();

  private AnalyticsAggregation       yAxisAggregation    = null;

  private long                       offset              = 0;

  private long                       limit               = 0;

  public List<AnalyticsAggregation> getAggregations() {
    List<AnalyticsAggregation> aggregations = new ArrayList<>();

    if (isMultipleCharts()) {
      aggregations.add(new AnalyticsAggregation(multipleChartsField));
    }

    aggregations.addAll(xAxisAggregations);

    if (yAxisAggregation != null && StringUtils.isNotBlank(yAxisAggregation.getField())) {
      aggregations.add(yAxisAggregation);
    }

    return aggregations;
  }

  public boolean isMultipleCharts() {
    return StringUtils.isNotBlank(multipleChartsField);
  }

  public void addEqualFilter(String field, String value) {
    AnalyticsFieldFilter fieldFilter = new AnalyticsFieldFilter(field, EQUAL, String.valueOf(value));
    this.filters.add(fieldFilter);
  }

  public void addInSetFilter(String field, String... values) {
    if (values != null && values.length > 0) {
      AnalyticsFieldFilter fieldFilter = new AnalyticsFieldFilter(field, IN_SET, new HashSet<>(Arrays.asList(values)));
      this.filters.add(fieldFilter);
    }
  }

  public void addRangeFilter(String field, double start, double end) {
    AnalyticsFieldFilter fieldFilter = new AnalyticsFieldFilter(field,
                                                                RANGE,
                                                                new Range(String.valueOf(start), String.valueOf(end)));
    this.filters.add(fieldFilter);
  }

  public void addGreaterFilter(String field, long value) {
    AnalyticsFieldFilter fieldFilter = new AnalyticsFieldFilter(field, GREATER, String.valueOf(value));
    this.filters.add(fieldFilter);
  }

  public void addLessFilter(String field, long value) {
    AnalyticsFieldFilter fieldFilter = new AnalyticsFieldFilter(field, LESS, String.valueOf(value));
    this.filters.add(fieldFilter);
  }
}
