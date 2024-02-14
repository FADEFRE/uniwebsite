<script setup>

const props = defineProps({
    statusDecision : {
        type: String,
        default: 'unedited'
    },
    gray: {
        type: Boolean,
        default: false
    },
    size: {
        type: String,
        default: 'mid'
    }
})
</script>

<template>
<svg v-if="statusDecision==='unedited'" class="status-icon" :class="size" viewBox="0 0 26 26" fill="none" xmlns="http://www.w3.org/2000/svg">
<circle cx="13" cy="13" r="12" class="gray" stroke-width="2"/>
<path class="gray-fill" d="M15.042 10.5C15.042 10.092 14.952 9.75 14.772 9.474C14.592 9.198 14.334 8.988 13.998 8.844C13.674 8.688 13.284 8.61 12.828 8.61C12.264 8.61 11.742 8.748 11.262 9.024C10.782 9.288 10.38 9.666 10.056 10.158L8.418 8.88C8.802 8.4 9.21 7.992 9.642 7.656C10.074 7.32 10.566 7.062 11.118 6.882C11.67 6.702 12.3 6.612 13.008 6.612C13.584 6.612 14.13 6.702 14.646 6.882C15.174 7.062 15.642 7.326 16.05 7.674C16.47 8.01 16.794 8.418 17.022 8.898C17.262 9.366 17.382 9.9 17.382 10.5C17.382 11.22 17.196 11.856 16.824 12.408C16.464 12.948 15.99 13.386 15.402 13.722C14.814 14.046 14.196 14.25 13.548 14.334L13.44 16.062H11.586L11.226 12.858H11.784C12.504 12.858 13.104 12.738 13.584 12.498C14.076 12.246 14.442 11.94 14.682 11.58C14.922 11.208 15.042 10.848 15.042 10.5ZM11.064 18.546C11.064 18.186 11.202 17.874 11.478 17.61C11.766 17.346 12.12 17.214 12.54 17.214C12.972 17.214 13.326 17.346 13.602 17.61C13.878 17.874 14.016 18.186 14.016 18.546C14.016 18.906 13.878 19.218 13.602 19.482C13.326 19.746 12.972 19.878 12.54 19.878C12.12 19.878 11.766 19.746 11.478 19.482C11.202 19.218 11.064 18.906 11.064 18.546Z" />
</svg>

<svg v-else-if="statusDecision==='accepted'" class="status-icon" :class="[size, gray ? 'gray' : 'green']" viewBox="0 0 26 26" fill="none" xmlns="http://www.w3.org/2000/svg">
<circle cx="13" cy="13" r="12" stroke-width="2"/>
<path d="M19 8L11.5 17L7 13" stroke-width="2"/>
</svg>


<svg v-else-if="statusDecision==='asExamCertificate'" class="status-icon" :class="[size, gray ? 'gray' : 'orange']" viewBox="0 0 26 26" fill="none" xmlns="http://www.w3.org/2000/svg">
<circle cx="13" cy="13" r="12" stroke-width="2"/>
<path d="M15.25 11.1667H10.75M15.25 14.8333H10.75M8.5 7.5H17.5V18.5H8.5V14.375V11.1667V7.5Z" stroke-width="2"/>
</svg>

<svg v-else-if="statusDecision==='denied'" class="status-icon" :class="[size, gray ? 'gray' : 'red']" viewBox="0 0 26 26" fill="none" xmlns="http://www.w3.org/2000/svg">
<circle cx="13" cy="13" r="12" stroke-width="2"/>
<path d="M8.5 17.5L13 13M17.5 8.5L13 13M13 13L17.5 17.5L8.5 8.5" stroke-width="2"/>
</svg>

<svg v-else-if="statusDecision==='formalRejection'" class="status-icon" :class="size" viewBox="0 0 26 26" fill="none" xmlns="http://www.w3.org/2000/svg">
<circle cx="13" cy="13" r="12" class="red" stroke-width="2"/>
<path class="red-fill" d="M11.53 6.4H14.41L13.888 15.022H12.052L11.53 6.4ZM11.494 18.01C11.494 17.614 11.632 17.29 11.908 17.038C12.196 16.774 12.55 16.642 12.97 16.642C13.402 16.642 13.756 16.774 14.032 17.038C14.308 17.29 14.446 17.614 14.446 18.01C14.446 18.406 14.308 18.736 14.032 19C13.756 19.252 13.402 19.378 12.97 19.378C12.55 19.378 12.196 19.252 11.908 19C11.632 18.736 11.494 18.406 11.494 18.01Z"/>
</svg>

</template>

<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.status-icon {
    
}

.mid {
    width: rem(26px);
    height: rem(26px);
}
.small {
    width: rem(24px);
    height: rem(24px);
}
 

.gray-fill {
    fill: $dark-gray;
    stroke: none;
}
.gray {
    stroke: $dark-gray;
}

.green {
    stroke: $green;
}
.orange {
    stroke: $orange;
}
.red-fill {
    fill: $red;
    stroke: none;
}
.red {
    stroke: $red;
}
</style>