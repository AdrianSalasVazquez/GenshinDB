package com.example.genshindb

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//       DATA CLASS DEL PERSONAJE
data class CharactersResponse (
    @SerializedName("rarity") var rarity: Int,
    @SerializedName("vision_key") var visionKey: String,
    @SerializedName("weapon_type") var weaponType: String,
    @SerializedName("constellation") var constellation: String,
    @SerializedName("birthday") var birthday: String,
    @SerializedName("nation") var nation: String,
    @SerializedName("gender") var gender: String,
    @SerializedName("es") var spanish: TranslatedCharactersResponse,
    @SerializedName("en") var english: TranslatedCharactersResponse,
    @SerializedName("stats") var stats: StatsCharactersResponse,
    @SerializedName("build") var build: BuildCharactersResponse,
    @SerializedName("res") var resources: ResourcesCharactersResponse
    ) : Serializable

//Datos que vienen traducidos
data class TranslatedCharactersResponse (
    @SerializedName("name") var name: String,
    @SerializedName("title") var title: String,
    @SerializedName("vision") var vision: String,
    @SerializedName("weapon") var weapon: String,
    @SerializedName("affiliation") var affiliation: String,
    @SerializedName("description") var description: String,

    @SerializedName("skillTalents") var skillTalents: SkillTalentsCharactersResponse,
    @SerializedName("passiveTalents") var passiveTalents: PassiveTalentsCharactersResponse,
    @SerializedName("constellations") var constellations: ConstellationsCharactersResponse
    ) : Serializable

//Datos de habilidades del personaje
data class SkillTalentsCharactersResponse (
    @SerializedName("normal_attack") var normal_attack: DataSkillCharactersResponse,
    @SerializedName("elemental_skill") var elemental_skill: DataSkillCharactersResponse,
    @SerializedName("elemental_burst") var elemental_burst: DataSkillCharactersResponse
) : Serializable
//Datos de talentos pasivos del personaje
data class PassiveTalentsCharactersResponse (
    @SerializedName("passive_1") var passive_1: DataSkillCharactersResponse,
    @SerializedName("passive_2") var passive_2: DataSkillCharactersResponse,
    @SerializedName("passive_basic") var passive_basic: DataSkillCharactersResponse
) : Serializable
//Datos de constelaciones del personaje
data class ConstellationsCharactersResponse (
    @SerializedName("c1") var c1: DataSkillCharactersResponse,
    @SerializedName("c2") var c2: DataSkillCharactersResponse,
    @SerializedName("c3") var c3: DataSkillCharactersResponse,
    @SerializedName("c4") var c4: DataSkillCharactersResponse,
    @SerializedName("c5") var c5: DataSkillCharactersResponse,
    @SerializedName("c6") var c6: DataSkillCharactersResponse
) : Serializable
//Datos habilidad general
data class DataSkillCharactersResponse (
    @SerializedName("name") var name: String,
    @SerializedName("unlock") var unlock: String,
    @SerializedName("description") var description: String
) : Serializable

//Estadisticas del personaje
data class StatsCharactersResponse (
    @SerializedName("base_hp") var base_hp: Double,
    @SerializedName("max_hp_value") var max_hp_value: Double,
    @SerializedName("base_atk") var base_atk: Double,
    @SerializedName("max_atk_value") var max_atk_value: Double,
    @SerializedName("base_def") var base_def: Double,
    @SerializedName("max_def_value") var max_def_value: Double
    ) : Serializable

//Build recomendada para el personaje
data class BuildCharactersResponse (
    @SerializedName("recommended_weapons") var recommended_weapons: List<String>,
    @SerializedName("recommended_artifacts") var recommended_artifacts: List<String>
) : Serializable

//Recursos audiovisuales
data class ResourcesCharactersResponse (
    @SerializedName("icon") var iconURL: String,
    @SerializedName("gacha_splash") var gachaSplashURL: String,
    @SerializedName("icon_talents") var iconTalents: TalentIconsCharactersResponse,
    @SerializedName("icon_constellations") var iconConstellations: ConstellationIconsCharactersResponse,
    ) : Serializable
//Iconos de los talentos
data class TalentIconsCharactersResponse (
    @SerializedName("icon_attack") var iconAttackURL: String,
    @SerializedName("icon_skill") var iconSkillURL: String,
    @SerializedName("icon_burst") var iconBurstURL: String,
    @SerializedName("icon_passive1") var iconPassive1URL: String,
    @SerializedName("icon_passive2") var iconPassive2URL: String,
    @SerializedName("icon_passive_basic") var iconPassiveBasicURL: String
    ) : Serializable
//Iconos de las constelaciones
data class ConstellationIconsCharactersResponse (
    @SerializedName("icon_c1") var iconC1URL: String,
    @SerializedName("icon_c2") var iconC2URL: String,
    @SerializedName("icon_c3") var iconC3URL: String,
    @SerializedName("icon_c4") var iconC4URL: String,
    @SerializedName("icon_c5") var iconC5URL: String,
    @SerializedName("icon_c6") var iconC6URL: String
    ) : Serializable




//       DATA CLASS DEL ARMA
data class WeaponsResponse(
    @SerializedName("type") var type: String,
    @SerializedName("rarity") var rarity: Int,
    @SerializedName("baseAttack") var baseAttack: Int,
    @SerializedName("subStat") var subStat: String,
    @SerializedName("es") var spanish: TranslatedWeaponsResponse,
    @SerializedName("en") var english: TranslatedWeaponsResponse,
    @SerializedName("res") var resources: ResourcesWeaponsResponse
    ) : Serializable
data class TranslatedWeaponsResponse(
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String,
    @SerializedName("passiveName") var passiveName: String,
    @SerializedName("passiveDesc") var passiveDesc: String,
    @SerializedName("location") var location: String
    ) : Serializable
data class ResourcesWeaponsResponse (
    @SerializedName("icon") var iconURL: String,
    @SerializedName("gacha_icon") var gachaIconURL: String
    ) : Serializable



//       DATA CLASS DEL ARTEFACTO
data class ArtifactsResponse(
    @SerializedName("max_rarity") var maxRarity: Int,
    @SerializedName("es") var spanish: TranslatedArtifactsResponse,
    @SerializedName("en") var english: TranslatedArtifactsResponse,
    @SerializedName("res") var resources: ResourcesArtifactsResponse
    ) : Serializable
data class TranslatedArtifactsResponse(
    @SerializedName("name") var name: String,
    @SerializedName("2-piece_bonus") var pieceBonus2: String,
    @SerializedName("4-piece_bonus") var pieceBonus4: String
    ) : Serializable
data class ResourcesArtifactsResponse(
    @SerializedName("flower") var flowerIconURL: String,
    @SerializedName("plume") var plumeIconURL: String,
    @SerializedName("sands") var sandsIconURL: String,
    @SerializedName("goblet") var gobletIconURL: String,
    @SerializedName("circlet") var circletIconURL: String
) : Serializable