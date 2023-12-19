<script>
export default {
    name: 'SideInfoContainer',
    props: {
        heading: {
            type: String,
            default: '',
        },
    },
};
</script>

<template>
    <div class="side-info-container">
        <h3>{{ heading }}</h3>
        <div class="slot-content">
            <slot></slot>
        </div>
    </div>
</template>


<style scoped>
</style>
