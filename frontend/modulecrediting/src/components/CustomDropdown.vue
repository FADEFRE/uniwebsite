<script setup>
import { ref } from "vue";
import ArrowIcon from "@/assets/icons/ArrowIcon.vue";

const dropdown = ref()
</script>

<template>
  <Dropdown ref="dropdown" @focus="dropdown.show()">
    <template #filtericon>
      <img class="search-icon" src="@/assets/icons/SearchIcon.svg">
    </template>
    <template #dropdownicon>
      <ArrowIcon color="white" direction="down" />
    </template>
  </Dropdown>
</template>

<style scoped lang="scss">
.search-icon {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  right: spacing(s);
}
</style>