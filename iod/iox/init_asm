; Directory Device Initialisation  V2.02    1989  Tony Tebby   QJUMP

        section iox

        xdef    iox_init

        xref    iou_ddst
        xref    iou_ddlk

        xref    iox_xint
        xref    iox_poll
        xref    iox_shed

        xref    iox_chek         ; check all slave blocks read
        xref    iox_flsh         ; flush all buffers
        xref    iox_occi         ; get occupancy information
        xref    iox_load         ; load
        xref    iox_save         ; save
        xref    iox_trnc         ; truncate
        xref    iox_lcbf         ; locate buffer
        xref    iox_albf         ; locate / allocate buffer
        xref    iox_upbf         ; mark buffer updated
        xref    iox_alfs         ; allocate first sector
        xref    iox_ckop         ; check medium for open operation
        xref    iox_fdrv         ; format drive
        xref    iox_rsec         ; read sector
        xref    iox_wsec         ; write sector

        include 'dev8_keys_iod'

;+++
; Initialise DD linkage and get device going.
;
;       status return standard
;---
iox_init
        move.l  a3,-(sp)                 ; save linkage pointer
        lea     iin_vect,a3              ; operations vector
        jsr     iou_ddst                 ; set dd linkage
        bne.s   iin_exit

; Fiddle around here

        jsr     iou_ddlk                 ; link in here

iin_exit
        move.l  (sp)+,a3
        rts


iin_vect
        dc.l    iod_jend    ; length of linkage
        dc.l    $00         ; length of physical definition
        dc.w    3,'XXX0'    ; device name (usage)
        dc.w    3,'XXX0'    ; device name

        dc.w    iox_xint-*  ; external interrupt server
        dc.w    iox_poll-*  ; polling server
        dc.w    iox_shed-*  ; scheduler server

        dc.w    0           ; io
        dc.w    0           ; open
        dc.w    0           ; close
        dc.w    iox_flsh-*  ; forced slaving
        dc.w    0           ; dummy
        dc.w    0           ; dummy
        dc.w    0           ; format

        dc.w    iox_chek-*  ; check all slave blocks read
        dc.w    iox_flsh-*  ; flush all buffers
        dc.w    iox_occi-*  ; get occupancy information
        dc.w    iox_load-*  ; load
        dc.w    iox_save-*  ; save
        dc.w    iox_trnc-*  ; truncate
        dc.w    iox_lcbf-*  ; locate buffer
        dc.w    iox_albf-*  ; locate / allocate buffer
        dc.w    iox_upbf-*  ; mark buffer updated
        dc.w    iox_alfs-*  ; allocate first sector
        dc.w    iox_ckop-*  ; check medium for open operation
        dc.w    iox_fdrv-*  ; format drive
        dc.w    iox_rsec-*  ; read sector
        dc.w    iox_wsec-*  ; write sector
        end
