<script>
export default {
    name: 'HomepageContainer',
    props: {
        header: {
            type: String,
            default: '',
        },
        text: {
            type: String,
            default: '',
        },
    
    },
};
</script>
<template>
    <div class="homepage-container" :style="{ 'border-left-color': borderLeftColor }">
        <div class="header">
            <h2 v-if="header">{{ header }}</h2>
            <h2 v-else>Platzhalter heading</h2>
        </div>
        <div class="text" v-if="text">
            <p v-html="text"></p>
        </div>
        <div class="text" v-else>
            <p>Platzhalter Text</p>
        </div>
        <div>
            <slot></slot>
        </div>
    </div>
</template>

<style>

</style>
