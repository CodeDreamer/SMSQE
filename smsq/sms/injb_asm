; Get information on a job   V2.00    1986  Tony Tebby  QJUMP

        section sms

        xdef    sms_injb                 ; information on job
        xdef    sms_nxjb                 ; next job in tree

        xref    sms_ckjx                 ; check job exists
        xref    sms_ckid                 ; check job id
        xref    sms_rte

        include 'dev8_keys_err'
        include 'dev8_keys_sys'
        include 'dev8_keys_jcbq'

;+++
; Information on job.
;***** NO!! If the Job ID passed is = 0, the the routine returns ERR.IJOB with
; d1 set to the ID of the first Job owned by d2. D3 and a0 are both returned 0.
;
;       d0  r   0 or invalid job
;       d1 cr   job ID / next job in tree
;       d2 cr   job at top of tree / job owner
;       d3  r   msbyte -ve if suspended, lsbyte is priority
;       a0  r   base of job
;       a6 c  p base of system variables
;
;       all other registers preserved
;---
sms_injb
        exg     d1,d2
        bsr.l   sms_ckid                 ; check if top possible
        exg     d1,d2
        bne.s   sij_exit                 ; oops

sij_exst
        bsr.l   sms_ckjx                 ; check if job exists
        bne.s   sij_exit                 ; ... oops
        move.l  d2,d0                    ; keep true top id safe

        move.l  jcb_ownr(a0),d2          ; ... job owner
        moveq   #0,d3                    ; not suspended
        tst.w   jcb_wait(a0)             ; waiting?
        beq.s   sij_prior                ; ... no
        not.l   d3                       ; yes, set suspended
sij_prior
        move.b  jcb_pinc(a0),d3          ; set priority byte
        lea     jcb_end(a0),a0           ; job base
        movem.l a0/a1,-(sp)              ; save volatiles
        bsr.s   sms_nxjb                 ; get next job
        movem.l (sp)+,a0/a1
        moveq   #0,d0                    ; set ok
sij_exit
        bra.l   sms_rte                  ; done
        page
;+++
; get next job id in d1 (top in d0)
;
;       d0 cr   top job id / 0
;       d1 cr   current job id / 0 or next job id
;       d7   s  current job id saved
;       a0  r   pointer to jcb of next
;       a1  r   pointer to job table for next
;---
sms_nxjb
        move.l  d1,d7                    ; keep current id

; scan job table looking for first daughter
        moveq   #1,d1                    ; start at 1 (Job 0 owned by 0!)
        bsr.s   snj_look                 ; looking for job owned by me
        beq.s   snj_exit                 ; ... found

; scan job table looking for next sibling

snj_up
        cmp.l   d0,d7                    ; at top?
        beq.s   snj_nnxt                 ; ... yes, no next
        move.l  d7,d1                    ; start at my owner
        beq.s   snj_nnxt                 ; ... can't
        lsl.w   #2,d1                    ; index table in long words
        move.l  sys_jbtb(a6),a0          ; base of job table
        add.w   d1,a0
        lsr.w   #2,d1
        move.l  (a0),a0                  ; job address
        move.l  jcb_ownr(a0),d7          ; my owner

        addq.w  #1,d1                    ; next job
        bsr.s   snj_look                 ; look for job owned by my owner
        beq.s   snj_exit                 ; ... found
        bra.s   snj_up                   ; ... no, up

snj_nnxt
        moveq   #0,d1                    ; no next job
        moveq   #0,d0
snj_exit
        rts


snj_look
        move.w  d1,a1                    ; job number
        add.w   a1,a1
        add.w   a1,a1
        add.l   sys_jbtb(a6),a1          ; pointer to job table

snj_loop
        cmp.w   sys_jbtp(a6),d1          ; off top of table?
        bgt.s   snj_rts                  ; ...
        tst.b   (a1)                     ; job exists?
        blt.s   snj_next
        move.l  (a1),a0                  ; jcb
        cmp.l   jcb_ownr(a0),d7          ; owned by correct job?
        beq.s   snj_stag                 ; ... yes
snj_next
        addq.l  #4,a1
        addq.w  #1,d1                    ; next
        bra.s   snj_loop

snj_stag
        swap    d1
        move.w  jcb_tag(a0),d1           ; tag in top end
        swap    d1
        moveq   #0,d0                    ; set Z
snj_rts
        rts
        end
