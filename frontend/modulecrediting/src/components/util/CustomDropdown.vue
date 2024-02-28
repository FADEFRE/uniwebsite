<script setup>
import { ref } from "vue";
import ArrowIcon from "@/assets/icons/ArrowIcon.vue";
import TrashIcon from "@/assets/icons/TrashIcon.vue";

const emit = defineEmits(['clear'])

const dropdown = ref()
</script>

<template>
  <Dropdown ref="dropdown" @focus="dropdown.show()">
    <template #clearicon>
      <TrashIcon @click="emit('clear')" background-color="dark-gray"/>
    </template>
    <template #filtericon>
      <img class="search-icon" src="../../assets/icons/SearchIcon.svg">
    </template>
    <template #dropdownicon>
      <ArrowIcon color="white" direction="down" />
    </template>
  </Dropdown>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.search-icon {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  right: spacing(s);
}
</style>