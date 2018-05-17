; HOTKEY JOB routines   V2.00     1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hk_cjob                  ; create job
        xdef    hk_rjob                  ; release job (supervisor mode)
        xdef    hk_kjob                  ; kill job (supervisor mode)
        xdef    hk_ckjob                 ; check job

        xref    hk_fitmk
        xref    hk_do
        xref    hk_rude

        xref.s  jb_princ                 ; priority increment

        include 'dev8_keys_jcb'
        include 'dev8_keys_err'
        include 'dev8_keys_qdos_sms'
        include 'dev8_ee_hk_data'
        include 'dev8_mac_assert'

;+++
; Kill hotkey job by setting program counter to suicide and releasing
;
;       a3 c  p Hotkey linkage block
;       status return zero
;---
hk_kjob
reg.kill reg    d1/d2/d3/a0/a1
        movem.l reg.kill,-(sp)
        move.w  sr,d0                    ; supervisor mode for this
        trap    #0
        move.w  d0,-(sp)
        bsr.s   hkj_rjob                 ; set address and release
        bne.s   hkk_ok                   ; it has gone!!
        lea     hkj_die,a1
        move.l  a1,jcb_pc-jcb_end(a0)    ; set PC to suicide
hkk_ok
        move.w  (sp)+,sr                 ; back to entry mode
        moveq   #0,d0
        movem.l (sp)+,reg.kill
        rts
;+++
; Release job, called in supervisor mode only!!
;
;       d0-d3 smashed
;       a0  r   job control block
;       a3 c  p Hotkey linkage block
;       status return arbitrary
;---
hk_rjob    ; supervisor mode only
hkj_rjob
        bsr.s   hkj_ckjb                 ; check job
        bne.s   hkj_rts
        move.b  #$7f,jb_princ-jcb_end(a0); set priority
        clr.w   jcb_wait-jcb_end(a0)     ; release job
hkj_rts
        rts

;+++
; Check status of job, if job has gone, it clears the job id and action in the
; linkage block
;
;       a3 c  p Hotkey linkage block
;       status return 0 or err.ijob
;---
hk_ckjob
reg.ckjb reg    d1-d3/a0
        movem.l reg.ckjb,-(sp)
        bsr.s   hkj_ckjb
        movem.l (sp)+,reg.ckjb
        rts
;+++
; Check status of job, if job has gone, it clears the job id and action in the
; linkage block. Internal version
;
;       d1   s
;       d2   s
;       d3  r   suspended / priority (as sms.jbin)
;       a0  r   job control block
;       a3 c  p Hotkey linkage block
;---
hkj_ckjb
        move.l  hkd_jbid(a3),d1          ; job ID
        beq.s   hkj_njob
        moveq   #sms.injb,d0             ; get job information
        moveq   #0,d2
        trap    #do.sms2
        tst.l   d0                       ; OK
        beq.s   hkj_rts

hkj_njob
        clr.l   hkd_jbid(a3)             ; no job now
        clr.w   hkd_act(a3)              ; no action or request
        moveq   #err.ijob,d0
        rts

        page
;+++
; create our own Hotkey job
;
;       a3 c  p Hotkey linkage block
;       status returns standard
;---
hk_cjob
reg.cjob reg    d1-d4/a0/a1/a4
        movem.l reg.cjob,-(sp)
        bsr.s   hk_kjob                  ; kill job!!
        lea     hkj_prog,a4
        moveq   #sms.crjb,d0             ; create job
        moveq   #0,d1                    ; independent
        moveq   #hkj_code-hkj_prog,d2    ; program size
        move.l  (a4)+,d3                 ; data space
        sub.l   a1,a1                    ; start address
        trap    #1
        tst.l   d0
        bne.s   hkc_oops                 ; ... no room

        move.l  d1,hkd_jbid(a3)          ; save job id
        move.l  a3,jcb_a3-jcb_end(a0)    ; set linkage address

        move.w  #$4ef9,(a0)+             ; JMP.L
        move.l  a4,(a0)+                 ; to start address

        addq.l  #6,a4
        moveq   #hkj_code-hkj_prog-6,d0  ; rest of program (just the name)
hkc_cname
        move.w  (a4)+,(a0)+              ; copy program name
        subq.w  #2,d0 
        bgt.s   hkc_cname

hkc_exit
        movem.l (sp)+,reg.cjob
        rts

hkc_oops
        jsr     hk_rude
        bra.s   hkc_exit

        page
hkj_prog
        dc.l    $180                     ; data space
        bra.s   hkj_code
        dc.w    0,0,$4afb,6,'HOTKEY'
hkj_code
        add.w   #$60,a6                  ; a6 is bottom of stack limit
hkj_loop
        st      hkd_act(a3)              ; job active

        move.w  hkd_key(a3),d1           ; next key operation
        ble.s   hkj_wait
        jsr     hk_fitmk                 ; point to item
        bne.s   hkj_bad
        jsr     hk_do                    ; do operation
hkj_bad
        sf      hkd_key+1(a3)            ; done
        beq.s   hkj_wait
        jsr     hk_rude

hkj_wait
        assert  hkd_act+1,hkd_req
        clr.w   hkd_act(a3)              ; job inactive
        moveq   #sms.ssjb,d0             ; suspend
        moveq   #-1,d1                   ; myself
        moveq   #-1,d3                   ; forever
        sub.l   a1,a1                    ; no flag address
        trap    #1
        bra.s   hkj_loop

hkj_die
        moveq   #sms.frjb,d0             ; I'm done for
        moveq   #-1,d1
        moveq   #0,d3
        trap    #do.sms2
        end
