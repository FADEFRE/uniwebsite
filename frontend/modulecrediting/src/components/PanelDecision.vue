<script setup>

</script>

<template>
  <div class="panel-decision">
    <h2>Entscheidung</h2>
    <slot name="study-office" />
    <slot name="chairman" />
  </div>
</template>

<style scoped lang="scss">
@import '@/assets/mixins.scss';
@import '@/assets/variables.scss';

.panel-decision {
  @include panelComponent();
}
</style>