<script setup>
import { ref } from 'vue';
import ButtonLink from "@/components/ButtonLink.vue";
import { postCourseLeipzig, postModuleLeipzig } from "@/scripts/axios-requests";

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
        .then(_ => location.reload())
        .catch(error => {
            if (error.response.status) {  // todo check for specific error
                courseExists.value = true
            } else {
                location.reload()
            }
        })
}
const createModuleLeipzig = () => {
    postModuleLeipzig(modulename.value, modulecode.value)
        .then(_ => location.reload())
        .catch(error => {
            if (error.response.status) {  // todo check for specific error
                moduleExists.value = true
            } else {
                location.reload()
            }
        })
}

</script>

<template>
    <div v-if="type === 'course'" class="management-create-container">
        <h2>Studiengang erstellen</h2>
        <div class="input-container">
            <InputText type="text" class="white" placeholder="Name des Studiengangs" v-model="coursename"
                :class="{ 'invalid': courseExists }" />
            <small v-if="courseExists" class="invalid-text">Studiengang existiert bereits</small>
        </div>
        <ButtonLink @click="createCourseLeipzig">Studiengang hinzufügen</ButtonLink>
    </div>


    <div v-else-if="type === 'module'" class="management-create-container">
        <h2>Modul erstellen</h2>
        <div class="input-container">
            <div class="module-input-container">
                <InputText type="text" class="white" placeholder="Modulname" v-model="modulename" :class="{ 'invalid': moduleExists }" />
                <InputText type="text" class="white" placeholder="Modulcode" v-model="modulecode" :class="{ 'invalid': moduleExists }" />
            </div>
            <small v-if="moduleExists" class="invalid-text">Studiengang existiert bereits</small>
        </div>
        <ButtonLink @click="createModuleLeipzig">Modul hinzufügen</ButtonLink>
    </div>
</template>


<style lang="scss" scoped>
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.management-create-container {
    @include basicContainer();
    @include verticalListItem($white);
    @include verticalList(l);
    width: 100%;
}

.module-input-container {
    @include screenSplit();
    width: 100%;
}

</style>