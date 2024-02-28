<script setup>
import { useRoute } from "vue-router";

const route = useRoute()
const heading = route.meta['error']['heading']
const content = route.meta['error']['content']
</script>

<template>
  <div class="main centered">
    <div class="error-container">
      <h1>{{ $t(heading) }}</h1>
      <p>{{ $t(content) }}</p>
    </div>
  </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.error-container {
  @include singleContainer();
  @include verticalList(big);
  display: flex;
  align-items: center;
  gap: spacing(xl);
}
</style>