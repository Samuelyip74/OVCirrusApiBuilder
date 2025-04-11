package com.al_enterprise.dataclasses

data class CaptivePortal(
    val captivePortalType: String? = null,
    val httpsRedirection: Boolean? = null
)

data class ClientSessionLogging(
    val clientSessionLoggingStatus: Boolean? = null,
    val loggingLevel: String? = null
)

data class WalledGarden(
    val allowDomains: List<String>? = null,
    val socialLoginVendor: List<String>? = null
)

data class AuthenticationStrategy(
    val aaaProfileId: Int? = null,
    val byPassStatus: Boolean? = null,
    val macAuthStatus: Boolean? = null
)

data class ClientControls(
    val e0211bStatus: Boolean? = null,
    val e0211gStatus: Boolean? = null,
    val maxClientsPerBand: Int? = null
)

data class HighThroughputControl(
    val enableAMpdu: Boolean? = null,
    val enableAMsdu: Boolean? = null
)

data class PowerSaveControls(
    val dtimInterval: Int? = null
)

data class BroadcastMulticastOptimization(
    val broadcastFilterARP: Boolean? = null,
    val broadcastFilterAll: Boolean? = null,
    val broadcastKeyRotation: Boolean? = null,
    val broadcastKeyRotationTimeInterval: Int? = null,
    val clientsNumber: Int? = null,
    val e0211rStatus: Boolean? = null,
    val multicastChannelUtilization: Int? = null,
    val multicastOptimization: Boolean? = null,
    val okcStatus: Boolean? = null
)

data class Dot1pMapping(
    val background: Dot1pMappingDetails? = null,
    val bestEffort: Dot1pMappingDetails? = null,
    val video: Dot1pMappingDetails? = null,
    val voice: Dot1pMappingDetails? = null
)

data class Dot1pMappingDetails(
    val downlinks: List<Int>? = null,
    val uplink: Int? = null
)

data class DscpMapping(
    val background: DscpMappingDetails? = null,
    val bestEffort: DscpMappingDetails? = null,
    val video: DscpMappingDetails? = null,
    val voice: DscpMappingDetails? = null,
    val trustOriginalDSCP: Boolean? = null
)

data class DscpMappingDetails(
    val downlinks: List<Int>? = null,
    val uplink: Int? = null
)

data class RoamingControls(
    val e0211kStatus: Boolean? = null,
    val e0211vStatus: Boolean? = null,
    val fdbUpdateStatus: Boolean? = null,
    val l3Roaming: Boolean? = null
)

data class Security(
    val classificationStatus: Boolean? = null,
    val clientIsolation: Boolean? = null,
    val macAuthPassProfileName: String? = null,
    val protectedManagementFrame: String? = null
)

data class Assignment(
    val group: Group? = null,
    val site: SiteInfo? = null
)

data class SSID(
    val accessRoleProfile: AccessRoleProfile? = null,
    val allowBand: String? = null,
    val mlo: Boolean? = null,
    val mloBand: String? = null,
    val authenticationStrategy: AuthenticationStrategy? = null,
    val byodRegistration: Boolean? = null,
    val clientControls: ClientControls? = null,
    val deviceSpecificPSK: String? = null,
    val dynamicPrivateGroupPSK: Boolean? = null,
    val dynamicVLAN: Boolean? = null,
    val enableSSID: Boolean? = null,
    val enhancedOpen: String? = null,
    val essid: String? = null,
    val frameControls802_11: FrameControls80211? = null,
    val guestPortal: Boolean? = null,
    val hideSSID: Boolean? = null,
    val highThroughputControl: HighThroughputControl? = null,
    val hotspot2: Hotspot2? = null,
    val id: Int? = null,
    val minClientDataRateControls: MinClientDataRateControls? = null,
    val minMgmtRateControls: MinMgmtRateControls? = null,
    val name: String? = null,
    val portalType: String? = null,
    val powerSaveControls: PowerSaveControls? = null,
    val privateGroupPSK: Boolean? = null,
    val qosSetting: QosSetting? = null,
    val replaceGroup: Boolean? = null,
    val roamingControls: RoamingControls? = null,
    val security: Security? = null,
    val securityLevel: String? = null,
    val securityLevelUI: String? = null,
    val uapsd: Boolean? = null,
    val useExistingAccessPolicy: Boolean? = null,
    val useExistingArp: Boolean? = null,
    val wepKeyIndex: Int? = null,
    val assignments: List<Assignment>? = null
)

data class AccessRoleProfile(
    val arpName: String? = null,
    val bandwidthControl: Map<String, Any>? = null,
    val captivePortal: CaptivePortal? = null,
    val childStatus: Boolean? = null,
    val clientIsolationAllowedList: List<String>? = null,
    val clientSessionLogging: ClientSessionLogging? = null,
    val dhcpOption82Status: Boolean? = null,
    val id: Int? = null,
    val saveAsDistinct: Boolean? = null,
    val useExistingAclAndQos: Boolean? = null,
    val useExistingLocationPolicy: Boolean? = null,
    val useExistingPeriodPolicy: Boolean? = null,
    val useExistingPolicyList: Boolean? = null,
    val walledGarden: WalledGarden? = null
)

data class FrameControls80211(
    val advApName: Boolean? = null
)

data class Hotspot2(
    val hotspot2Status: Boolean? = null
)

data class MinClientDataRateControls(
    val minRate24G: Int? = null,
    val minRate24GStatus: Boolean? = null,
    val minRate5G: Int? = null,
    val minRate5GStatus: Boolean? = null,
    val minRate6G: Int? = null,
    val minRate6GStatus: Boolean? = null
)

data class MinMgmtRateControls(
    val minRate24G: Int? = null,
    val minRate24GStatus: Boolean? = null,
    val minRate5G: Int? = null,
    val minRate5GStatus: Boolean? = null,
    val minRate6G: Int? = null,
    val minRate6GStatus: Boolean? = null
)

data class QosSetting(
    val bandwidthContract: Map<String, Any>? = null,
    val broadcastMulticastOptimization: BroadcastMulticastOptimization? = null,
    val dot1pMapping: Dot1pMapping? = null,
    val dot1pMappingEnable: Boolean? = null,
    val dscpMapping: DscpMapping? = null,
    val dscpMappingEnable: Boolean? = null
)


data class SiteInfo(
    val status: Int? = null,
    val message: String? = null,
    val data: List<SSID>? = null
)


data class SSIDResponse(
    val status: Int? = null,
    val message: String? = null,
    val data: List<SSID>? = null
)
