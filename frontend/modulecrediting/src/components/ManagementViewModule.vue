<script setup>
import ManagementListElement from "./ManagementListElement.vue";
import { ref, onBeforeMount, computed } from "vue";
import { getModulesNameCode, deleteModuleLeipzig, putUpdateModuleLeipzig } from "@/scripts/axios-requests";

const modules = ref();

const searchString = ref('');

onBeforeMount(() => {
    getModulesNameCode()
        .then(data => modules.value = data)
})

const filteredModules = computed(() => {
    if (searchString.value === '') return modules.value;

    return modules.value.filter(singleModule => {
        return singleModule.name.toLocaleLowerCase().includes(searchString.value.toLocaleLowerCase());
    })
})

const triggerEditModuleLeipzig = (oldName, newName, newCode) => {
  putUpdateModuleLeipzig(oldName, newName, newCode)
      .then(_ => location.reload())
}

const triggerDeleteModuleLeipzig = (module) => {
    deleteModuleLeipzig(module)
        .then(_ => location.reload())
}
</script>

<template>
    <div class="view-module-container">
        <h2>ALLE MODULE</h2>
        <div class="search-container">
            <InputText v-model="searchString" placeholder="Modul suchen"></InputText>
            <img src="@/assets/icons/SearchIcon.svg" class="search-icon">
        </div>
        <div v-for="singleModule in filteredModules" class="module-item">
          <ManagementListElement
              :name="singleModule['name']"
              :code="singleModule['code']"
              :edit-callback="triggerEditModuleLeipzig"
              :delete-callback="triggerDeleteModuleLeipzig"
          />
        </div>
    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.view-module-container {
    @include basicContainer();
}

.search-container {
    @include searchFieldContainer();
}

.module-item {
  @include smallHighlightBox();
  @include verticalListItem($gray);
  width: 100%;
}
</style>