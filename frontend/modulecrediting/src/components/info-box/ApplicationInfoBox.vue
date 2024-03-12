<script setup>
import ArrowIcon from "@/assets/icons/ArrowIcon.vue";
import { ref } from "vue";

const showInformation = ref(false)
const showExample = ref(false)
</script>

<template>
  <div class="application-info-container">
    <h2>{{ $t('ApplicationInfoBox.Heading') }}</h2>
    <div class="explanation-collapsed-container">
      <p class="text-justify">{{ $t('ApplicationInfoBox.BasicExplanation') }}</p>
    </div>
    <Button @click="showInformation = !showInformation" class="info-box-button">
      {{ $t('ApplicationInfoBox.Explanation.ShowExplanation') }}
      <ArrowIcon :direction="showInformation ? 'up' : 'down'" color="red" />
    </Button>
    <div v-if="showInformation" class="explanation-expanded-container">
      <div class="explanation-item">
        <h3 class="h4">{{ $t('ApplicationInfoBox.Explanation.ModuleAssignment.Heading') }}</h3>
        <ul class="points">
          <li>{{ $t('ApplicationInfoBox.Explanation.ModuleAssignment.0') }}</li>
          <li>{{ $t('ApplicationInfoBox.Explanation.ModuleAssignment.1') }}</li>
          <li>{{ $t('ApplicationInfoBox.Explanation.ModuleAssignment.2') }}</li>
        </ul>
      </div>
      <div class="explanation-item">
        <h3 class="h4">{{ $t('ApplicationInfoBox.Explanation.ExternalModules.Heading') }}</h3>
        <ul class="points">
          <li>{{ $t('ApplicationInfoBox.Explanation.ExternalModules.0') }}</li>
          <li>{{ $t('ApplicationInfoBox.Explanation.ExternalModules.1') }}</li>
          <li>{{ $t('ApplicationInfoBox.Explanation.ExternalModules.2') }}</li>
          <li>{{ $t('ApplicationInfoBox.Explanation.ExternalModules.3') }}</li>
        </ul>
      </div>
      <div class="explanation-item">
        <h3 class="h4">{{ $t('ApplicationInfoBox.Explanation.ModulesLeipzig.Heading') }}</h3>
        <ul class="points">
          <li>{{ $t('ApplicationInfoBox.Explanation.ModulesLeipzig.0') }}</li>
          <li>{{ $t('ApplicationInfoBox.Explanation.ModulesLeipzig.1') }}</li>
        </ul>
      </div>
      <div class="explanation-item">
        <h3 class="h4">{{ $t('ApplicationInfoBox.Explanation.Comment.Heading') }}</h3>
        <ul class="points">
          <li>{{ $t('ApplicationInfoBox.Explanation.Comment.0') }}</li>
        </ul>
      </div>
    </div>
    <Button @click="showExample = !showExample" class="info-box-button">
      {{ $t('ApplicationInfoBox.Example.ShowExample') }}
      <ArrowIcon :direction="showExample ? 'up' : 'down'" color="red" />
    </Button>
    <div v-if="showExample" class="explanation-expanded-container">
      <div class="explanation-item">
        <h3 class="h4">
          {{ $t('ApplicationInfoBox.Example.ModuleAssignment.Heading') }}
        </h3>
        <ul class="points">
          <li>
            "{{ $t('ApplicationInfoBox.Example.ModuleAssignment.ExternalModules') }}
            <span :aria-label="$t('ApplicationInfoBox.Example.ModuleAssignment.AriaCreditedFor')">
              <ArrowIcon direction="right" color="red" />
            </span>
            {{ $t('ApplicationInfoBox.Example.ModuleAssignment.ModulesLeipzig') }}"
          </li>
        </ul>
      </div>
      <div class="explanation-item">
        <h4>{{ $t('ApplicationInfoBox.Example.ExternalModules.Heading') }}</h4>
        <ul class="points">
          <li>{{ $t('ApplicationInfoBox.Example.ExternalModules.0') }}</li>
          <li>{{ $t('ApplicationInfoBox.Example.ExternalModules.1') }}</li>
          <li>{{ $t('ApplicationInfoBox.Example.ExternalModules.2') }}</li>
          <li>{{ $t('ApplicationInfoBox.Example.ExternalModules.3') }}</li>
          <li>{{ $t('ApplicationInfoBox.Example.ExternalModules.4') }}</li>
        </ul>
      </div>
      <div class="explanation-item">
        <h4>{{ $t('ApplicationInfoBox.Example.ModulesLeipzig.Heading') }}</h4>
        <ul class="points">
          <li>{{ $t('ApplicationInfoBox.Example.ModulesLeipzig.0') }}</li>
          <li>{{ $t('ApplicationInfoBox.Example.ModulesLeipzig.1') }}</li>
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