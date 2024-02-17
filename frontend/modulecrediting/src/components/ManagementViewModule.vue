<script setup>
import { getModulesNameCode, deleteModuleLeipzig } from "@/scripts/axios-requests";
import { ref, onBeforeMount, computed } from "vue";
import TrashIcon from "../assets/icons/TrashIcon.vue";
import ManagementListElement from "./ManagementListElement.vue";

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

const deleteModuleLeipzigClick = (singleModule) => {
    deleteModuleLeipzig(singleModule.name)
        .then(_ => location.reload())
}

// todo suche machen
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
              :id="singleModule['id']"
              :delete-callback="function (id) {}"
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