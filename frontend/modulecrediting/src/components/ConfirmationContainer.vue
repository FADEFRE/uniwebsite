<script setup>
import { computed, ref } from 'vue';
import { useClipboard } from '@vueuse/core'
import CopyIcon from '../assets/icons/CopyIcon.vue';

const props = defineProps({
    id: {
        type: String,
        required: true
    }
});

const { copy, copied, isSupported } = useClipboard({ props })

const formattedId = computed(() => formatId(props.id));

const copyId = () => {
    copy(props.id);
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
                <div class="copy-icon-container" @click=copyId>
                    <CopyIcon v-if="isSupported" :disabled="copied"/>
                </div>
            </div>
            <p class="description-text">Mit der Vorgangsnummer kannst du immer den Status deines Antrags überprüfen</p>
        </div>
        <div class="button-container">
            <slot></slot>
        </div>
    </div>
</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.confirmation-container {
    @include singleContainer();
}

.button-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: spacing(m);
}

.id-section {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: spacing(m);
    width: min-content;
}

.id-container {
    position: relative;
    width: max-content;
    padding: spacing(m) spacing(xl);
    background-color: $gray;
    border: solid 1px $dark-gray;
}
.id {
    font-size: 2rem;
    font-weight: 800;
    letter-spacing: 0.5rem;
}
.copy-icon-container {
    position: absolute;
    top: 0;
    right: 0;
    padding: spacing(s);
}

.description-text {
    text-align: center;
}
</style>
