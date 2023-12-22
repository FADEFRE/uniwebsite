<script setup>
import { useRoute, useRouter } from 'vue-router';
import { ref, onBeforeMount } from 'vue';
import { getApplicationByIdForStatus } from '@/scripts/axios-requests';
import { url } from '@/scripts/url-config';
import axios from 'axios';
import ConfirmationContainer from '../components/ConfirmationContainer.vue';

const route = useRoute();
const router = useRouter();
const isInvalid = ref(false);
const applicationData = ref(undefined);
let id = undefined;
let pdfDataLink = undefined;

onBeforeMount(() => {
    id = route.params.id;
    pdfDataLink = `${url}/applications/pdf-data/${id}`;
    getApplicationByIdForStatus(id)
        .then(data => applicationData.value = data)
        .catch(error => {
            console.log(error);
            applicationData.value = 'error';
        });
});

const openDetailView = () => {
    axios.get(`${url}/applications/${id}/exists`)
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

const copyId = () => {
    navigator.clipboard.writeText(id);
};
</script>

<template>
    <div class="confirmation-container" v-if="applicationData">
        <ConfirmationContainer :id="id">
            <img @click="copyId" class="copy-icon" src="@/assets/icons/CopyIcon.svg" alt="Copy Icon">
            <Button @click="openPdf" class="pdf-button">PDF Öffnen/herunterladen</Button>
            <Button @click="openDetailView" class="status-button">Status einsehen</Button>
        </ConfirmationContainer>
    </div>
</template>

<style scoped></style>
