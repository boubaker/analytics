package org.exoplatform.analytics.api.service;

import java.util.List;
import java.util.Set;

import org.exoplatform.analytics.es.FieldMapping;
import org.exoplatform.analytics.model.StatisticData;
import org.exoplatform.analytics.model.chart.ChartDataList;
import org.exoplatform.analytics.model.filter.AnalyticsFilter;

public abstract class AnalyticsService {

  private AnalyticsQueueService queueService;

  public AnalyticsService(AnalyticsQueueService queueService) {
    this.queueService = queueService;
  }

  void create(StatisticData data) {
    if (data == null) {
      throw new IllegalArgumentException("Statistic data to store is mandatory");
    }

    queueService.put(data);
  }

  public abstract ChartDataList getChartData(AnalyticsFilter filter);

  public abstract List<StatisticData> getData(AnalyticsFilter searchFilter);

  public abstract int count(AnalyticsFilter searchFilter);

  /**
   * @param forceRefresh whether force refresh from ES or not
   * @return a {@link Set} of ES mapping fields
   */
  public abstract Set<FieldMapping> retrieveMapping(boolean forceRefresh);

}