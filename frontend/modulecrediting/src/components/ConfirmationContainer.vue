<script setup>
const props = defineProps({
    id: {
        type: String,
        required: true
    }
});

const copyId = () => {
    navigator.clipboard.writeText(props.id);
};
</script>

<template>
    <div class="confirmation-container">
        <div class="id-section">
            <div class="id-container">
                <h2 class="id"> 3-4-5-6-7-8</h2>
                <img @click="copyId" class="copy-icon" src="@/assets/icons/CopyIcon.svg" alt="Copy Icon">
            </div>
            <p class="description-text">Mit der Vorgangsnummer kannst du immer den Status deines Antrags überprüfen</p>
        </div>
        <div class="button-container">
            <slot></slot>
        </div>
    </div>
</template>

<style scoped lang="scss">
@import '../assets/mixins.scss';
@import '../assets/variables.scss';

.confirmation-container {
    @include singleContainer();
}

.button-container {
    display: flex;
    flex-direction: column;
    gap: 0.9rem;
}

.id-section {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 0.9375rem;
    width: min-content;
}

.id-container {
    position: relative;
    width: max-content;
    padding: 0.9375rem 1.875rem;
    background-color: $gray;
    border: solid 1px $dark-gray;
}
.id {
    font-size: 2.25rem;
    font-weight: 800;
    letter-spacing: 0.5625rem;
}
.copy-icon {
    position: absolute;
    top: 0.5rem;
    right: 0.5rem;
}

.description-text {
    text-align: center;
}
</style>
