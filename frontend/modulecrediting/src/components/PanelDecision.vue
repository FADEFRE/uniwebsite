<script setup>

</script>

<template>
  <div class="panel-decision">
    <h4>Entscheidung</h4>
    <div class="decision-block-container">
      <slot name="study-office" />
      <slot name="chairman" />
    </div>
  </div>
</template>

<style scoped lang="scss">
@import '@/assets/mixins.scss';
@import '@/assets/variables.scss';

.panel-decision {
  @include panelComponent();
}

.decision-block-container {
  @include screenSplit();
  width: 100%;
}
</style>