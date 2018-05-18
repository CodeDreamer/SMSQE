; the basic name definitions for soundfile et al  v. 1.00 (c) 2012 W. Lenerz


	section sound


	include 'dev8_mac_proc'
	xdef	snd_nam

	section procs

snd_nam lea	procs,a1
	move.w	$110,a2
	jmp	(a2)
procs
	proc_stt
	proc_def	SOUNDFILE
	proc_def	SOUNDFILE2
	proc_def	SOUNDFILE3
	proc_def	KILLSOUND
	proc_end
	proc_stt
	proc_def	ALFM
	proc_def	FREE_FMEM
	proc_end

	end
