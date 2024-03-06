<script setup>
import ArrowIcon from "@/assets/icons/ArrowIcon.vue";
import { ref } from "vue";

const showInformation = ref(false)
const showExample = ref(false)
</script>

<template>
  <div class="application-info-container">
    <h2>{{ $t('ApplicationInfoBox.Information') }}</h2>
    <div class="explanation-collapsed-container">
      <p class="text-justify">{{ $t('ApplicationInfoBox.Explanation') }}</p>
    </div>
    <Button @click="showInformation = !showInformation" class="info-box-button">
      {{ $t('ApplicationInfoBox.ShowExplanation') }}
      <ArrowIcon :direction="showInformation ? 'up' : 'down'" color="red" />
    </Button>
    <div v-if="showInformation" class="explanation-expanded-container">
      <div class="explanation-item">
        <h3 class="h4">{{ $t('ApplicationInfoBox.ModuleAssignment') }}</h3>
        <ul class="points">
          <li>{{ $t('ApplicationInfoBox.ForeignModule') }}</li>
          <li>{{ $t('ApplicationInfoBox.belongingModule') }}</li>
          <li>{{ $t('ApplicationInfoBox.multipleAssignments') }}</li>
        </ul>
      </div>
      <div class="explanation-item">
        <h3 class="h4">{{ $t('ApplicationInfoBox.Foreign') }}</h3>
        <ul class="points">
          <li>{{ $t('ApplicationInfoBox.exactModule') }}</li>
          <li>{{ $t('ApplicationInfoBox.FillFields') }}</li>
          <li>{{ $t('ApplicationInfoBox.CreditSystem') }}</li>
          <li>{{ $t('ApplicationInfoBox.uploadPDF') }}</li>
        </ul>
      </div>
      <div class="explanation-item">
        <h3 class="h4">{{ $t('ApplicationInfoBox.ModulesLeipzig') }}</h3>
        <ul class="points">
          <li>{{ $t('ApplicationInfoBox.ChoseModule') }}</li>
          <li>{{ $t('ApplicationInfoBox.ChoseNone') }}</li>
        </ul>
      </div>
      <div class="explanation-item">
        <h3 class="h4">{{ $t('ApplicationInfoBox.Comment') }}</h3>
        <ul class="points">
          <li>{{ $t('ApplicationInfoBox.FurtherInformation') }}</li>
        </ul>
      </div>
    </div>
    <Button @click="showExample = !showExample" class="info-box-button">
      {{ $t('ApplicationInfoBox.ShowExample') }}
      <ArrowIcon :direction="showExample ? 'up' : 'down'" color="red" />
    </Button>
    <div v-if="showExample" class="explanation-expanded-container">
      <div class="explanation-item">
        <h3 class="h4">
          {{ $t('ApplicationInfoBox.ModuleAssignment') }}
        </h3>
        <ul class="points">
          <li>
            "Automaten und Berechenbarkeit
            <ArrowIcon direction="right" color="red" :aria-label="$t('ApplicationInfoBox.CreditedFor')" />
            Automaten und Sprachen, Berechenbarkeit"
          </li>
        </ul>
      </div>
      <div class="explanation-item">
        <h4>{{ $t('ApplicationInfoBox.Foreign') }}</h4>
        <ul class="points">
          <li>{{ $t('ApplicationInfoBox.ModuleName') }}</li>
          <li>{{ $t('ApplicationInfoBox.UniHalle') }}</li>
          <li>{{ $t('ApplicationInfoBox.Credits') }}</li>
          <li>{{ $t('ApplicationInfoBox.ModuleDescription') }}</li>
        </ul>
      </div>
      <div class="explanation-item">
        <h4>{{ $t('ApplicationInfoBox.ModulesLeipzig') }}</h4>
        <ul class="points">
          <li>{{ $t('ApplicationInfoBox.AutomataandLanguages') }}</li>
          <li>{{ $t('ApplicationInfoBox.Predictability') }}</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.explanation-collapsed-container {
  @include verticalList(s);

}
.explanation-expanded-container {
  @include verticalList(s);
  // border-bottom: 2px solid $dark-gray;
  margin-bottom: spacing(s);
  padding: spacing(xs);
}
</style>