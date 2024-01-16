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
    background-color: $gray;
    color: $dark-gray;
    @include smallHighlightBox();

    &:hover {
        background-color: $mid-gray;
    }
}

.active {
    color: $white;
    background-color: $dark-gray;
}
</style>