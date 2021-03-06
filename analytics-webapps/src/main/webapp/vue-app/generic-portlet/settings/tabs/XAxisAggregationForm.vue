<template>
  <v-form
    ref="form"
    class="aggregationForm"
    @submit="
      $event.preventDefault();
      $event.stopPropagation();
    ">
    <v-card-text>
      <v-layout wrap>
        <template>
          <v-flex
            class="my-auto px-2"
            xs4>
            <v-radio-group v-model="dateInterval">
              <v-radio
                v-for="dateField in dateFields"
                :key="dateField.value"
                :label="dateField.text"
                :value="dateField.value" />
              <v-radio label="Per custom field(s)" value="custom" />
            </v-radio-group>
          </v-flex>
          <v-flex
            v-if="!dateAggregationType"
            class="my-auto px-2"
            xs8>
            <v-layout v-for="(item, index) in xAxisAggregations" :key="index">
              <v-flex xs10>
                <field-selection
                  v-model="item.field"
                  :fields-mappings="fieldsMappings"
                  label="Field name"
                  aggregation />
              </v-flex>
              <v-flex class="my-auto" xs1>
                <v-btn icon @click="deleteAggregation(index)">
                  <v-icon>fa-minus-circle</v-icon>
                </v-btn>
              </v-flex>
              <v-flex
                v-if="(index +1) === xAxisAggregations.length"
                class="my-auto"
                xs1>
                <v-btn icon @click="addAggregation">
                  <v-icon>fa-plus-circle</v-icon>
                </v-btn>
              </v-flex>
            </v-layout>
          </v-flex>
        </template>
      </v-layout>
    </v-card-text>
  </v-form>
</template>

<script>
import FieldSelection from '../form/FieldSelection.vue';

export default {
  components: {
    FieldSelection,
  },
  props: {
    settings: {
      type: Object,
      default: function() {
        return null;
      },
    },
    fieldsMappings: {
      type: Array,
      default: function() {
        return [];
      },
    },
  },
  data: () => ({
    dateFields: [
      {
        text: 'Per day',
        value: 'day',
      },
      {
        text: 'Per week',
        value: 'week',
      },
      {
        text: 'Per month',
        value: 'month',
      },
      {
        text: 'Per quarter',
        value: 'quarter',
      },
      {
        text: 'Per year',
        value: 'year',
      },
    ],
    dateAggregationType: true,
    dateInterval: 'custom',
    dateAggregation: {
      type: 'DATE',
      field: 'timestamp',
      sortDirection: 'asc',
      interval: 'day',
    },
    fieldAggregation: {
      type: 'COUNT',
      field: 'module',
      sortDirection: 'asc',
    },
  }),
  computed: {
    xAxisAggregations() {
      return this.settings && this.settings.xAxisAggregations;
    },
    hasAggregations() {
      return this.xAxisAggregations && this.xAxisAggregations.length;
    }
  },
  watch: {
    dateInterval(newVal, oldVal) {
      if (oldVal !== newVal) {
        this.dateAggregationType = this.dateInterval !== 'custom';
        this.changeAggregationType();
      }
    }
  },
  mounted() {
    if (!this.xAxisAggregations || !this.xAxisAggregations.length) {
      this.dateInterval = 'day';
    } else {
      this.dateAggregationType = this.xAxisAggregations && this.xAxisAggregations.length === 1 && this.xAxisAggregations[0].type === 'DATE';
      if (this.dateAggregationType) {
        this.dateInterval = this.xAxisAggregations[0].interval;
      }
    }
  },
  methods: {
    changeAggregationType() {
      if (this.dateAggregationType) {
        const dateAggregation = Object.assign({}, this.dateAggregation);
        dateAggregation.interval = this.dateInterval;
        this.settings.xAxisAggregations.splice(0, this.settings.xAxisAggregations.length, dateAggregation);
      } else {
        this.settings.xAxisAggregations.splice(0, this.settings.xAxisAggregations.length, Object.assign({}, this.fieldAggregation));
      }
      this.$forceUpdate();
    },
    deleteAggregation(aggregationIndex) {
      this.xAxisAggregations.splice(aggregationIndex, 1);
    },
    addAggregation() {
      this.xAxisAggregations.push({type: 'COUNT'});
      this.$forceUpdate();
    },
  },
};
</script>