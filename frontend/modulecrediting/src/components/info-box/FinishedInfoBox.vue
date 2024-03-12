<script setup>

import ModuleStatusIcon from "@/assets/icons/ModuleStatusIcon.vue";
</script>

<template>
  <div class="application-info-container">
    <h2>{{ $t('FinishedInfoBox.Heading') }}</h2>
    <p class="text-justify">
      {{ $t('FinishedInfoBox.Text') }}
    </p>
    <div class="legend-container">
      <h3 class="h4">{{ $t('FinishedInfoBox.Explanation') }}</h3>
      <ul>
        <li class="explanation-list-item">
          <ModuleStatusIcon status-decision="accepted" size="small" aria-hidden="true" />
          <p>{{ $t('IconModuleStatus.Accepted') }}</p>
        </li>
        <li class="explanation-list-item">
          <ModuleStatusIcon status-decision="asExamCertificate" size="small" aria-hidden="true" />
          <p>{{ $t('IconModuleStatus.AsExamCertificate') }}</p>
        </li>
        <li class="explanation-list-item">
          <ModuleStatusIcon status-decision="denied" size="small" aria-hidden="true" />
          <p>{{ $t('IconModuleStatus.Denied') }}</p>
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