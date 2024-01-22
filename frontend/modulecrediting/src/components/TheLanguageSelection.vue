<script setup>
</script>

<template>
    <div class="language-selection-container">
        <Button @click="$i18n.locale = 'DE'" class="language-button" :class="{ 'active': $i18n.locale == 'DE' }">DE</Button>
        <Button @click="$i18n.locale = 'EN'" class="language-button" :class="{ 'active': $i18n.locale == 'EN' }">EN</Button>
    </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.language-selection-container {
    display: flex;
}
.language-button {
    background-color: transparent;
    padding: 0 0.5rem;

    color: $white;
    font-family: "Jost";
    font-size: 1.125rem;
    font-weight: 400;
    line-height: 1rem;

    &:first-child {
        border-right: 1px solid $white;
    }

    &:hover {
        background-color: transparent;
        font-weight: 550;
    }


}

.active {
    font-weight: 550;
}
</style>