<script setup>
import { useUserStore } from "@/store/userStore";
import translate from '@/i18n/translate';

const store = useUserStore()

</script>

<template>
    <div class="language-selection-container">
        <Button @click="translate.switchLanguage('de')" class="language-button" :class="{ 'active': store.locale == 'de' }">DE</Button>
        <Button @click="translate.switchLanguage('en')" class="language-button" :class="{ 'active': store.locale == 'en' }">EN</Button>
    </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.language-selection-container {
    width: fit-content;
    display: flex;
}
.language-button {
    background-color: transparent;
    padding: 0 0.5rem;

    color: $white;
    font-family: "Jost";
    font-size: 1rem;
    font-weight: 400;
    line-height: 1.25rem;

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