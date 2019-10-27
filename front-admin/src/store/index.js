import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'
Vue.use(Vuex)

export const HTTP = axios.create({
    baseURL: 'http://192.168.43.214:8080/api/',
    headers: {'Content-Type': 'application/json'}
});
export default new Vuex.Store({
        state: {
            command: null,
        },
        mutations: {
            setCommand(state, command) {
                state.command = command
            },
        },
        actions: {
            async loadCommand({commit}) {
                try {
                    const command = await HTTP.get('commands/all')
                    commit('setCommand', command.data)

                } catch (error) {
                    commit('setCommand', null)
                    throw error
                }
            },
            async postWord({commit}, word) {
                try {
                    await HTTP.post('commands', word)
                    const command = await HTTP.get('commands/all')
                    commit('setCommand', command.data)
                } catch (error) {
                    commit('setCommand', null)
                    throw error
                }
            }
        },
        getters: {
            command(state) {
                return state.command
            }
        }
    }
)