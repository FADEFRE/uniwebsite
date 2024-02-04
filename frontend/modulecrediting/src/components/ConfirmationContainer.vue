<script setup>
import { computed, ref } from 'vue';

const props = defineProps({
    id: {
        type: String,
        required: true
    }
});

const formattedId = computed(() => formatId(props.id));

const iconClicked = ref(false);
const copyId = () => {
    navigator.clipboard.writeText(props.id);
    iconClicked.value = true;
};

function formatId(id) {
    return id.replace(/(\d)(?=(\d{1,5})+$)/g, '$1-');
}
</script>

<template>
    <div class="confirmation-container">
        <div class="id-section">
            <div class="id-container">
                <h2 class="id">{{ formattedId }}</h2>
                <img @click="copyId" :class="{ 'icon-clicked': iconClicked }" class="copy-icon" src="@/assets/icons/CopyIcon.svg" alt="Copy Icon">
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
    align-items: center;
    gap: 1rem;
}

.id-section {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 1rem;
    width: min-content;
}

.id-container {
    position: relative;
    width: max-content;
    padding: 1rem 2rem;
    background-color: $gray;
    border: solid 1px $dark-gray;
}
.id {
    font-size: 2rem;
    font-weight: 800;
    letter-spacing: 0.5rem;
}
.copy-icon {
    position: absolute;
    top: 0.5rem;
    right: 0.5rem;

    transition: 0.05s ease-in-out;

    &:hover {
        transform: scale(1.1);
    }

    &.icon-clicked {
        opacity: 0.5;
        transform: scale(1);
    }
}

.description-text {
    text-align: center;
}
</style>
