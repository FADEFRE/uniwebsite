<script setup>
import { useUserStore } from "@/store/userStore";
import translate from '@/i18n/translate';
import { useRoute } from "vue-router";
import { computed } from "vue";

const store = useUserStore()
const route = useRoute()
const type = route.meta['authType']
const showLanguage = computed(() => {
    if (type !== "standard") { return false }
    return true
});

</script>

<template>
    <div v-if="showLanguage" class="language-selection-container">
        <Button @click="translate.switchLanguage('de')" class="language-button" :class="{ 'active': store.locale == 'de' }">DE</Button>
        <Button @click="translate.switchLanguage('en')" class="language-button" :class="{ 'active': store.locale == 'en' }">EN</Button>
    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;

.language-selection-container {
    width: fit-content;
    display: flex;
}
.language-button {
    background-color: transparent;
    padding: 0 spacing(s);

    color: $white;
    font-family: "Jost";
    font-size: 1rem;
    font-weight: 400;
    line-height: spacing(m);

    &:first-child {
        border-right: 1px solid $white;
    }

    &:hover {
        background-color: transparent;
        text-decoration: underline;
    }
}


.active {
    font-weight: 550;
    &:hover {
        text-decoration: none;
    }
}
</style>