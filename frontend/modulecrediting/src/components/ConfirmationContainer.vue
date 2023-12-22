<script setup>
import { defineProps } from 'vue';

const props = defineProps({id: {
        type: String,
        required: true
    }
});
</script>

<template>
    <div>
        <div>
            <p>Die Vorgangsnummer:</p>
            <h2 v-if="id">{{ id }}</h2>
            <h2 v-else>Platzhalter heading</h2>
        </div>
        <div>
            <p>Mit der Vorgangsnummer kannst du immer den Status deines Antrags überprüfen</p>
        </div>
        <div>
            <slot></slot>
        </div>
    </div>
</template>

<style></style>
