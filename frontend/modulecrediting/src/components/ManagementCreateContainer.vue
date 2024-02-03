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

const courseExists = ref(false);
const moduleExists = ref(false);

const createCourseLeipzig = () => {
    postCourseLeipzig(coursename.value)
        .then((result) => {
            if (result === 'exists') {
                courseExists.value = true
                return   
            }
            coursename.value = '';
            location.reload();
        })
}
const createModuleLeipzig = () => {
    postModuleLeipzig(modulename.value, modulecode.value)
        .then((result) => {
            if (result === 'exists') {
                moduleExists.value = true
                return
            }
            modulename.value = '';
            modulecode.value = '';
            location.reload();
        })
}

</script>

<template>
    <div v-if="type === 'course'" class="management-create-container">
        <h2>Studiengang erstellen</h2>
        <div class="input-container">
            <InputText type="text" placeholder="Name des Studiengangs" v-model="coursename" :class="{ 'invalid': courseExists }" />
            <small v-if="courseExists" class="invalid-text">Studiengang existiert bereits</small>
        </div>
        <ButtonAdd @click="createCourseLeipzig">Studiengang hinzufügen</ButtonAdd>
    </div>


    <div v-else-if="type === 'module'" class="management-create-container">
        <h2>Modul erstellen</h2>
        <div class="input-container">
            <div class="module-input-container">
                <InputText type="text" placeholder="Modulname" v-model="modulename" :class="{ 'invalid': moduleExists}"/>
                <InputText type="text" placeholder="Modulcode" v-model="modulecode" :class="{ 'invalid': moduleExists }"/>
            </div>
            <small v-if="moduleExists" class="invalid-text">Studiengang existiert bereits</small>
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
    width: 100%
}
.module-input-container {
    @include screenSplit();
    width: 100%;
}

.p-inputtext {
    width: 100%;

    &:hover {
        background-color: $white-hover;
    }
}

.invalid {
  border: 2px solid $red;
}

.invalid-text {
  color: $red;
}
</style>