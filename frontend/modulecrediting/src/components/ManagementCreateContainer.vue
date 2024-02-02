<script setup>
import ButtonAdd from "@/components/ButtonAdd.vue";
import { ref } from 'vue';
import { postCourseLeipzig, postModuleLeipzig } from "@/scripts/axios-requests";
import _default from "pinia-plugin-persistedstate";
const props = defineProps({
    type: {
        required: true,
        type: String
    }
});

const coursename = ref('');
const modulename = ref('');
const modulecode = ref('');

const createCourseLeipzig = () => {
    postCourseLeipzig(coursename.value)
        .then((result) => {
            if (result) {
                coursename.value = '';
                location.reload();
            }
        })
}
const createModuleLeipzig = () => {
    postModuleLeipzig(modulename.value, modulecode.value)
        .then((result) => {
            if (result) {
                modulename.value = '';
                modulecode.value = '';
                location.reload();
            }
        })
}

</script>

<template>
    <div v-if="type === 'course'" class="management-create-container">
        <h2>Studiengang erstellen</h2>
        <InputText type="text" placeholder="Name des Studiengangs" v-model="coursename" />
        <ButtonAdd @click="createCourseLeipzig">Studiengang hinzufügen</ButtonAdd>
    </div>


    <div v-else-if="type === 'module'" class="management-create-container">
        <h2>Modul erstellen</h2>
        <div class="input-container">
            <InputText type="text" placeholder="Modulname" v-model="modulename" />
            <InputText type="text" placeholder="Modulcode" v-model="modulecode" />
        </div>
        <ButtonAdd @click="createModuleLeipzig">Modul hinzufügen</ButtonAdd>
    </div>
</template>


<style lang="scss" scoped>
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.management-create-container {
    @include basicContainer();
    @include verticalListItem($white);
    @include verticalList(big);
    width: 100%;
}

.input-container {
    @include screenSplit();
    width: 100%;
}

.p-inputtext {
    width: 100%;

    &:hover {
        background-color: $white-hover;
    }
}
</style>