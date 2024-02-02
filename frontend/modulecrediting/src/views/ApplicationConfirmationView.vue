<script setup>
import { useRoute, useRouter } from 'vue-router';
import { ref, onBeforeMount } from 'vue';
import { getApplicationByIdForStatus } from '@/scripts/axios-requests';
import { url } from '@/scripts/url-config';
import httpResource from "@/scripts/httpResource";
import ConfirmationContainer from '../components/ConfirmationContainer.vue';
import ButtonLink from "@/components/ButtonLink.vue";

const route = useRoute();
const router = useRouter();
const isInvalid = ref(false);
const applicationData = ref(undefined);
let id = undefined;
let pdfDataLink = undefined;

onBeforeMount(() => {
    id = route.params.id;
    pdfDataLink = `${url}/file/pdf-documents/application/${id}`;
    getApplicationByIdForStatus(id)
        .then(data => applicationData.value = data)
        .catch(error => {
            console.log(error);
            applicationData.value = 'error';
        });
});

const openDetailView = () => {
    httpResource.get(`${url}/api/applications/${id}/exists`)
        .then(response => {
            if (response.data) {
                const routeData = router.resolve({ name: 'statusDetail', params: { id: id } });
                window.open(routeData.href, '_top');
            } else {
                isInvalid.value = true;
            }
        });
};

const openPdf = () => {
    window.open(pdfDataLink, '_blank');
};


</script>

<template>
    <div class="main" v-if="applicationData">
        <ConfirmationContainer :id="id">
            <Button @click="openPdf">
                Antrag herunterladen
                <img src="../assets/icons/Download.svg">
            </Button>
            <ButtonLink @click="openDetailView" class="status-button">Status einsehen</ButtonLink>
        </ConfirmationContainer>
    </div>
</template>

<style scoped lang="scss">
@import '../assets/variables.scss';
@import '../assets/mixins.scss';

.main {
    @include main();
}
</style>
