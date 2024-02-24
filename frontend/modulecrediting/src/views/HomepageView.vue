<script setup>
import { useRouter } from 'vue-router'
import { ref } from 'vue'
import HomepageContainer from '@/components/HomepageContainer.vue';
import SideInfoContainer from '@/components/SideInfoContainer.vue';
import ButtonLink from '@/components/ButtonLink.vue';
import { getApplicationExists } from "@/scripts/axios-requests";
import ApplicationProcessSideInfo from "@/components/ApplicationProcessSideInfo.vue";

const router = useRouter()

const isInvalid = ref(false)
const id = ref('')

const openDetailView = () => {
    const formattedId = getFormattedId();
    console.log(id.value);
    console.log(formattedId);
    getApplicationExists(formattedId)
        .then(data => {
            if (data === false) {
                isInvalid.value = true;
                return;
            }

            const routeData = router.resolve({ name: 'statusDetail', params: { id: formattedId } });
            window.open(routeData.href, '_top');
        })
}

const goToSubmitApplication = () => {
    router.push({ name: 'submitApplication' })
}

const handleEnterKey = (event) => {
    if (event.key === 'Enter') {
        openDetailView();
    }
}

const validateInput = () => {
    id.value = id.value.replace(/[^0-9]/g, '');
    id.value = id.value.replace(/(\d)(?=(\d{1,5})+$)/g, '$1-');
    id.value = id.value.slice(0, 11);
}

const getFormattedId = () => {
    return id.value.replace(/-/g, '');
}

</script>

<template>
    <div class="main">
        <h1 class="screen-reader-only">Startseite</h1>

        <div class="content-container split">
            <!-- HomepageContainer Application -->
            <HomepageContainer :header="$t('homepage.makeApplication')" :text="$t('homepage.makeApplicationExplanation')">
                <ButtonLink @click="goToSubmitApplication">{{ $t('homepage.makeApplication') }}</ButtonLink>
            </HomepageContainer>

            <!-- HomepageContainer StatusView -->
            <HomepageContainer :header="$t('homepage.viewStatus')" :text="$t('homepage.viewStatusExplanation')">
                <div class="input-button-container">
                    <InputText v-model="id" :class="{ 'invalid': isInvalid }" class="status-input gray"
                        placeholder="0-0-0-0-0-0" @keydown.enter.prevent="openDetailView" @input.prevent="validateInput" />
                    <ButtonLink @click="openDetailView">{{ $t('homepage.viewStatus') }}</ButtonLink>
                </div>
                <small v-if="isInvalid" class="invalid-text">Dieser Vorgang existiert nicht</small>
            </HomepageContainer>
        </div>

        <div class="side-infos-list">
           <ApplicationProcessSideInfo />
        </div>

    </div>
</template>


<style scoped lang="scss">
@use '@/assets/styles/util' as *;
@use '@/assets/styles/global' as *;
@use '@/assets/styles/components' as *;

.status-input {
    border: none;
    background-color: $gray;

    &:focus::placeholder {
        color: transparent;
    }

    &.invalid {
        border: 2px solid $red;
    }
}
</style>
