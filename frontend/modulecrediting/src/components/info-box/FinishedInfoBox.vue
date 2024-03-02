<script setup>

import ModuleStatusIcon from "@/assets/icons/ModuleStatusIcon.vue";
</script>

<template>
  <div class="application-info-container">
    <h2>{{ $t('StatusDetailView.WhatNext') }}</h2>
    <p class="text-justify">
      {{ $t('StatusDetailView.FinalDecisionExplanation') }}
    </p>
    <div class="legend-container">
      <h3 class="h4">{{ $t('StatusDetailView.legend') }}</h3>
      <ul>
        <li class="explanation-list-item">
          <ModuleStatusIcon status-decision="accepted" size="small"/>
          <p>{{ $t('StatusDetailView.CreditingAccepted') }}</p>
        </li>
        <li class="explanation-list-item">
          <ModuleStatusIcon status-decision="asExamCertificate" size="small"/>
          <p>{{ $t('StatusDetailView.CreditingAsAdmission') }}</p>
        </li>
        <li class="explanation-list-item">
          <ModuleStatusIcon status-decision="denied" size="small"/>
          <p>{{ $t('StatusDetailView.CreditingRejected') }}</p>
        </li>
      </ul>
    </div>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.legend-container {
  @include verticalList(s);
}

.explanation-list-item {
  @include smallHighlightBox();
  @include verticalListItem($gray);
  justify-content: flex-start;
}
</style>